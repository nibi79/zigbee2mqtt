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
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttPropertyDiscovery} is a service for discovering channels
 *
 * @author Nils
 */
public class Zigbee2MqttPropertyDiscovery extends Zigbee2MqttDiscovery {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttPropertyDiscovery.class);

    private String ieeeAddr = null;
    private Map<String, String> discoveryResult = new HashMap<>();

    /**
     * @param bridgeHandler
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttPropertyDiscovery(Zigbee2MqttBridgeHandler bridgeHandler) throws IllegalArgumentException {

        super(bridgeHandler, bridgeHandler.getTopicHandler().getTopicBridgeConfigDevices(),
                bridgeHandler.getTopicHandler().getTopicBridgeConfigDevicesGet());
    }

    /**
     * @param thing
     * @return
     */
    public Map<String, String> discover(String ieeeAddr) {

        this.ieeeAddr = ieeeAddr;

        startDiscovery();

        return discoveryResult;
    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        JsonArray message = jsonMessage.get("message").getAsJsonArray();

        for (JsonElement jsonElement : message) {

            String discoveredIeeeAddr = jsonElement.getAsJsonObject().get("ieeeAddr").getAsString();

            if (ieeeAddr.equals(discoveredIeeeAddr)) {

                for (Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {

                    if (entry.getValue().isJsonPrimitive()) {

                        logger.debug("{} - property discovered: key={}, value={}", entry.getKey(),
                                entry.getValue().getAsString());

                        discoveryResult.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }

            }
        }

    }

}
