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

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttMessageSubscriber;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttTopicHandler;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttBridgeDiscoveryService} is a service for discovering your zigbee2mqttServer on localhost(1883)
 *
 * @author Nils
 */
@Component(service = DiscoveryService.class, immediate = true, configurationPid = "binding.zigbee2mqtt")
public class Zigbee2MqttBridgeDiscoveryService extends AbstractDiscoveryService
        implements Zigbee2MqttMessageSubscriber {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttBridgeDiscoveryService.class);

    MqttBrokerConnection mqttBrokerConnection = null;

    /**
     * Maximum time to search for server in seconds.
     */
    private static final int SEARCH_TIME = 10;

    /**
     *
     */
    public Zigbee2MqttBridgeDiscoveryService() {
        super(SUPPORTED_THING_TYPES, SEARCH_TIME);

        mqttBrokerConnection = new MqttBrokerConnection("localhost", Integer.valueOf(1883), false,
                "openHAB.zigbee2mqtt.discovery");
        mqttBrokerConnection.start();

    }

    /**
     * Public method for triggering server discovery
     */
    public void discover() {
        startScan();
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return SUPPORTED_THING_TYPES;
    }

    @Override
    protected void startBackgroundDiscovery() {
        startScan();
    }

    @Override
    protected void startScan() {

        mqttBrokerConnection.subscribe(new Zigbee2MqttTopicHandler().getTopicBridgeConfigDevices(), this);
        mqttBrokerConnection.publish(new Zigbee2MqttTopicHandler().getTopicBridgeConfigDevicesGet(), "get".getBytes());
    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        JsonArray message = jsonMessage.get("message").getAsJsonArray();

        for (JsonElement jsonElement : message) {

            String ieeeAddr = jsonElement.getAsJsonObject().get("ieeeAddr").getAsString();
            String type = jsonElement.getAsJsonObject().get("type").getAsString();

            if ("Coordinator".equals(type)) {

                logger.debug("server discovered [ieeeAddr=" + ieeeAddr + "]");

                ThingUID thingUID = new ThingUID(THING_TYPE_GATEWAY, ieeeAddr);

                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                        .withLabel("Zigbee2MqttServer - " + ieeeAddr).build();

                thingDiscovered(discoveryResult);
            }
        }

    }

}
