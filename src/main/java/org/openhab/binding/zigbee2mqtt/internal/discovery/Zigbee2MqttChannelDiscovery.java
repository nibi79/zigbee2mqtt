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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.ChannelDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.TopicHomeassistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttChannelDiscovery} is a service for discovering channels
 *
 * @author Nils
 */
public class Zigbee2MqttChannelDiscovery extends Zigbee2MqttDiscovery<ChannelDiscovery> {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttChannelDiscovery.class);

    /**
     * @param bridgeHandler
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttChannelDiscovery(Zigbee2MqttBridgeHandler bridgeHandler) throws IllegalArgumentException {
        super(bridgeHandler);
    }

    @Override
    protected List<ChannelDiscovery> processResultSet(HashMap<TopicHomeassistant, JsonObject> discoveryResult) {

        HashMap<String, ChannelDiscovery> channels = new HashMap<>();

        for (Entry<TopicHomeassistant, JsonObject> entry : discoveryResult.entrySet()) {

            TopicHomeassistant topic = entry.getKey();

            ChannelDiscovery channelDiscovery = new ChannelDiscovery(topic.getTopic(), topic.getIeeeAddr());
            channelDiscovery.setId(topic.getId());
            channelDiscovery.setCategory(topic.getCategory());

            // channel config
            JsonObject json = entry.getValue();
            for (Entry<String, JsonElement> prop : json.entrySet()) {

                if (prop.getValue().isJsonPrimitive()) {
                    channelDiscovery.getConfig().put(prop.getKey(), prop.getValue().getAsString());
                }
            }

            channels.put(channelDiscovery.getId(), channelDiscovery);

            logger.debug("result entry processed: {}", channelDiscovery.toString());
        }

        return new ArrayList<ChannelDiscovery>(channels.values());
    }

}
