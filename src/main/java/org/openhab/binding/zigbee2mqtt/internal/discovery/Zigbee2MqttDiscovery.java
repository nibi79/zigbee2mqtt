/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.zigbee2mqtt.internal.discovery;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.DiscoveryResult;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.TopicHomeassistant;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttDiscovery} is a service for discovering channels
 *
 * @author Nils
 */
public abstract class Zigbee2MqttDiscovery<T extends DiscoveryResult> implements Zigbee2MqttMessageSubscriber {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDiscovery.class);

    protected static final String TOPIC_CHANNELDISCOVERY = TopicHomeassistant.TOPIC_HOMEASSISTANT;

    protected Zigbee2MqttBridgeHandler bridgeHandler = null;

    private String ieeeAddr = null;

    private HashMap<TopicHomeassistant, JsonObject> discoveryResult = new HashMap<>();

    /**
     * @param bridgeHandler
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttDiscovery(Zigbee2MqttBridgeHandler bridgeHandler) throws IllegalArgumentException {
        this.bridgeHandler = bridgeHandler;
    }

    /**
     * @param thing
     * @return
     */
    public List<T> discover(String ieeeAddr) {

        this.ieeeAddr = ieeeAddr;

        List<T> result = null;

        boolean accomplished = runDiscovery();

        if (accomplished) {

            result = processResultSet(discoveryResult);
        }
        return result;
    }

    /**
     * Process the resultset which contains config messages and convert it to list of DiscoveryResults.
     *
     * @param ieeeAddr
     * @param discoveryResult
     * @return
     */
    protected abstract List<T> processResultSet(HashMap<TopicHomeassistant, JsonObject> discoveryResult);

    /**
     * Subscribes to homeassistant topic to recieve configs .
     *
     * @param thing
     * @return
     */
    private boolean runDiscovery() {

        if (bridgeHandler == null) {
            return false;
        }
        // Trigger no discovery if offline
        if (bridgeHandler.getThing().getStatus().equals(ThingStatus.OFFLINE)) {
            return false;
        }

        try {

            logger.debug("mqtt: subribe to topic '{}'", TOPIC_CHANNELDISCOVERY);
            bridgeHandler.subscribe(TOPIC_CHANNELDISCOVERY, this);

            // pause until channels discovered form homeassistant topic
            Thread.sleep(2000);

            return true;

        } catch (InterruptedException e) {

            logger.warn(e.getMessage(), e);
            return false;
        } finally {

            logger.debug("mqtt: unsubribe from topic '{}'", TOPIC_CHANNELDISCOVERY);
            bridgeHandler.unsubscribe(TOPIC_CHANNELDISCOVERY, this);
        }

    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        TopicHomeassistant topicHomeassistant = new TopicHomeassistant(topic);
        if (ieeeAddr.equals(topicHomeassistant.getIeeeAddr())) {

            discoveryResult.put(topicHomeassistant, jsonMessage);
        }
    }

}
