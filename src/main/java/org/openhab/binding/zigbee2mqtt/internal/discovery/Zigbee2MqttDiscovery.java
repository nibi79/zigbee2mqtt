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

import org.eclipse.smarthome.core.thing.ThingStatus;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Zigbee2MqttDiscovery} is a service for discovering channels
 *
 * @author Nils
 */
public abstract class Zigbee2MqttDiscovery implements Zigbee2MqttMessageSubscriber {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDiscovery.class);

    protected Zigbee2MqttBridgeHandler bridgeHandler = null;
    private String discoveryTopic = null;
    private String triggerTopic = null;

    /**
     * @param bridgeHandler
     * @param discoveryTopic
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttDiscovery(Zigbee2MqttBridgeHandler bridgeHandler, String discoveryTopic)
            throws IllegalArgumentException {
        this.bridgeHandler = bridgeHandler;
        this.discoveryTopic = discoveryTopic;
    }

    /**
     * @param bridgeHandler
     * @param discoveryTopic
     * @param triggerTopic
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttDiscovery(Zigbee2MqttBridgeHandler bridgeHandler, String discoveryTopic, String triggerTopic)
            throws IllegalArgumentException {
        this.bridgeHandler = bridgeHandler;
        this.discoveryTopic = discoveryTopic;
        this.triggerTopic = triggerTopic;
    }

    /**
     * Subscribes to homeassistant topic to recieve configs .
     *
     * @param thing
     * @return
     */
    protected void startDiscovery() {

        if (bridgeHandler == null) {
            return;
        }
        // Trigger no discovery if offline
        if (bridgeHandler.getThing().getStatus().equals(ThingStatus.OFFLINE)) {
            return;
        }

        try {

            logger.debug("mqtt: subribe to topic '{}'", discoveryTopic);
            bridgeHandler.subscribe(discoveryTopic, this);

            if (triggerTopic != null) {
                // trigger message
                bridgeHandler.publish(triggerTopic, "get");
            }

            // pause until channels discovered form discovery topic
            Thread.sleep(2000);

        } catch (InterruptedException e) {

            logger.warn(e.getMessage(), e);

        } finally {

            logger.debug("mqtt: unsubribe from topic '{}'", discoveryTopic);
            bridgeHandler.unsubscribe(discoveryTopic, this);
        }

    }

}
