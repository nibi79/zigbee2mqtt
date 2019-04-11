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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.PropertyDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.DiscoveryTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttPropertyDiscovery} is a service for discovering channels
 *
 * @author Nils
 */
public class Zigbee2MqttPropertyDiscovery extends Zigbee2MqttDiscovery<PropertyDiscovery> {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttPropertyDiscovery.class);

    /**
     * @param bridgeHandler
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttPropertyDiscovery(Zigbee2MqttBridgeHandler bridgeHandler) throws IllegalArgumentException {
        super(bridgeHandler);
    }

    @Override
    protected List<PropertyDiscovery> processResultSet(HashMap<DiscoveryTopic, JsonObject> discoveryResult) {

        HashMap<String, PropertyDiscovery> props = new HashMap<>();

        for (Entry<DiscoveryTopic, JsonObject> entry : discoveryResult.entrySet()) {

            JsonObject deviceinfo = entry.getValue().get("device").getAsJsonObject();

            if (deviceinfo != null) {
                DiscoveryTopic key = entry.getKey();
                addProperties(key.getTopic(), key.getIeeeAddr(), props, deviceinfo);
            }
        }

        return new ArrayList<PropertyDiscovery>(props.values());
    }

    /**
     * Add properties to list from given json.
     *
     * @param props
     * @param json
     */
    private void addProperties(String topic, String ieeeAddr, HashMap<String, PropertyDiscovery> props,
            JsonObject json) {

        for (Entry<String, JsonElement> entry : json.entrySet()) {

            if (entry.getValue().isJsonPrimitive()) {

                PropertyDiscovery propertyDiscovery = new PropertyDiscovery(topic, ieeeAddr);

                propertyDiscovery.setKey(entry.getKey());
                propertyDiscovery.setValue(entry.getValue().getAsString());
                props.put(entry.getKey(), propertyDiscovery);

                logger.debug("result entry processed: {}", propertyDiscovery.toString());
            } else {

                addProperties(topic, ieeeAddr, props, entry.getValue().getAsJsonObject());
            }
        }
    }

}
