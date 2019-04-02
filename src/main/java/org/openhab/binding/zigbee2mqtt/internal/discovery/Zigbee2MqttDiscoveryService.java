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

import static org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBridgeHandler;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttDiscoveryService} is a service for discovering your cameras through Synology API
 *
 * @author Nils
 */
// @Component(service = Zigbee2MqttDiscoveryService.class, immediate = true, configurationPid = "discovery.zgbee2mqtt")
public class Zigbee2MqttDiscoveryService extends AbstractDiscoveryService implements Zigbee2MqttMessageSubscriber {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDiscoveryService.class);

    private Zigbee2MqttBridgeHandler bridgeHandler = null;

    /**
     * Maximum time to search for devices in seconds.
     */
    private static final int SEARCH_TIME = 20;

    /**
     *
     */
    public Zigbee2MqttDiscoveryService() {
        super(SUPPORTED_THING_TYPES, SEARCH_TIME);
    }

    /**
     * @param bridgeHandler
     * @throws IllegalArgumentException
     */
    public Zigbee2MqttDiscoveryService(Zigbee2MqttBridgeHandler bridgeHandler) throws IllegalArgumentException {
        super(SUPPORTED_THING_TYPES, SEARCH_TIME);
        this.bridgeHandler = bridgeHandler;
    }

    /**
     * Public method for triggering device discovery
     */
    public void discover() {
        startScan();
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return SUPPORTED_THING_TYPES;
    }

    @Override
    protected void startScan() {

        // try {

        if (bridgeHandler == null) {
            return;
        }
        // Trigger no scan if offline
        if (bridgeHandler.getThing().getStatus().equals(ThingStatus.OFFLINE)) {
            return;
        }

        bridgeHandler.subscribe(bridgeHandler.getMqttbrokerBaseTopic() + "/bridge/log", this);

        bridgeHandler.publish(bridgeHandler.getMqttbrokerBaseTopic() + "/bridge/config/devices", "ds");

    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        String messageType = jsonMessage.get("type").getAsString();

        if ("devices".equals(messageType)) {

            JsonArray message = jsonMessage.get("message").getAsJsonArray();

            ThingUID bridgeUID = bridgeHandler.getThing().getUID();

            for (JsonElement jsonElement : message) {

                String ieeeAddr = jsonElement.getAsJsonObject().get("ieeeAddr").getAsString();
                String type = jsonElement.getAsJsonObject().get("type").getAsString();
                String model = jsonElement.getAsJsonObject().get("model").getAsString().replace(".", "_");
                String friendlyName = jsonElement.getAsJsonObject().get("friendly_name").getAsString();

                ThingUID thingUID = new ThingUID(THING_TYPE_DEVICE, bridgeUID, ieeeAddr);

                Map<String, Object> properties = new HashMap<>();
                properties.put("ieeeAddr", ieeeAddr);
                properties.put("type", type);
                properties.put("model", model);
                properties.put("friendly_name", friendlyName);

                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                        .withBridge(bridgeHandler.getThing().getUID()).withProperties(properties)
                        .withLabel(friendlyName + " (" + model + ")").build();

                thingDiscovered(discoveryResult);

            }

        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        startScan();
    }

}
