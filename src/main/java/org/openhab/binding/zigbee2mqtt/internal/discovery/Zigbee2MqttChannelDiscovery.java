/**
 * Copyright (c) 2014,2018 by the respective copyright holders.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.zigbee2mqtt.internal.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.ChannelDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.DiscoveryTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttChannelDiscovery} is a service for discovering channels
 *
 * @author Nils
 */
public class Zigbee2MqttChannelDiscovery extends Zigbee2MqttDiscovery {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttChannelDiscovery.class);

    private String ieeeAddr = null;
    private List<ChannelDiscovery> discoveryResult = new ArrayList<>();

    /**
     * @param bridgeHandler
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttChannelDiscovery(Zigbee2MqttBridgeHandler bridgeHandler) throws IllegalArgumentException {

        super(bridgeHandler, bridgeHandler.getTopicHandler().getDiscoveryTopic() + "/#");
    }

    /**
     * @param thing
     * @return
     */
    public List<ChannelDiscovery> discover(String ieeeAddr) {

        this.ieeeAddr = ieeeAddr;

        startDiscovery();

        return discoveryResult;
    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        DiscoveryTopic topicHomeassistant = new DiscoveryTopic(topic);
        if (ieeeAddr.equals(topicHomeassistant.getIeeeAddr())) {

            ChannelDiscovery channelDiscovery = new ChannelDiscovery(topicHomeassistant.getTopic(),
                    topicHomeassistant.getIeeeAddr());
            channelDiscovery.setObjectId(topicHomeassistant.getObjectId());
            channelDiscovery.setType(topicHomeassistant.getType());

            // channel config
            for (Entry<String, JsonElement> prop : jsonMessage.entrySet()) {

                if (prop.getValue().isJsonPrimitive()) {
                    channelDiscovery.getConfig().put(prop.getKey(), prop.getValue().getAsString());
                }
            }

            logger.debug("{} - channel discovered: {}", ieeeAddr, channelDiscovery.toString());

            discoveryResult.add(channelDiscovery);
        }
    }
}
