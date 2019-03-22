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
package org.openhab.binding.zigbee2mqtt.internal;

import static org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBindingConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.zigbee2mqtt.internal.discovery.Zigbee2MqttChannelConverter;
import org.openhab.binding.zigbee2mqtt.internal.discovery.Zigbee2MqttChannelDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.discovery.Zigbee2MqttPropertyDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.ChannelDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.PropertyDiscovery;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/***
 * The{@link Zigbee2MqttDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author zigbee2mqtt - Initial contribution
 */
@NonNullByDefault
public class Zigbee2MqttDeviceHandler extends BaseThingHandler implements Zigbee2MqttMessageSubscriber {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDeviceHandler.class);

    public Zigbee2MqttDeviceHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        // if (CHANNEL_1.equals(channelUID.getId())) {
        // if (command instanceof RefreshType) {
        // // TODO: handle data refresh
        // }
        //
        // // TODO: handle command
        //
        // // Note: if communication with thing fails for some reason,
        // // indicate that by setting the status with detail information:
        // // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
        // // "Could not control device at IP address x.x.x.x");
        // }
    }

    @Override
    public void initialize() {

        Zigbee2MqttBridgeHandler bridgeHandler = getZigbee2MqttBridgeHandler();

        if (bridgeHandler != null) {
            String ieeeAddr = getThing().getUID().getId();

            bridgeHandler.getMqttBrokerConnection().subscribe(bridgeHandler.getMqttbrokerBaseTopic() + "/" + ieeeAddr,
                    this);

            ThingBuilder thingBuilder = editThing();

            // discover properties
            Zigbee2MqttPropertyDiscovery propertyDiscovery = new Zigbee2MqttPropertyDiscovery(bridgeHandler);
            List<PropertyDiscovery> propertyDiscoveries = propertyDiscovery.discover(ieeeAddr);
            Map<String, String> props = new HashMap<String, String>();
            for (PropertyDiscovery prop : propertyDiscoveries) {
                props.put(prop.getKey(), prop.getValue());
            }

            // discover channels
            Zigbee2MqttChannelDiscovery channelDiscovery = new Zigbee2MqttChannelDiscovery(bridgeHandler);
            List<ChannelDiscovery> channelDiscoveries = channelDiscovery.discover(ieeeAddr);
            Zigbee2MqttChannelConverter channelConverter = new Zigbee2MqttChannelConverter();
            List<Channel> channels = channelConverter.convert(channelDiscoveries, thing);

            updateThing(thingBuilder.withProperties(props).withChannels(channels).build());

            updateStatus(ThingStatus.ONLINE);
        } else {

            updateStatus(ThingStatus.OFFLINE);
        }
    }

    protected synchronized @Nullable Zigbee2MqttBridgeHandler getZigbee2MqttBridgeHandler() {
        Bridge bridge = getBridge();
        if (bridge == null) {
            return null;
        }
        ThingHandler handler = bridge.getHandler();
        if (handler instanceof Zigbee2MqttBridgeHandler) {
            return (Zigbee2MqttBridgeHandler) handler;
        } else {
            return null;
        }
    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        String thingId = getThing().getUID().getId();
        logger.debug("incoming message for ThingUID: {}", thingId);

        for (Entry<String, JsonElement> entry : jsonMessage.entrySet()) {

            if (entry.getValue().isJsonPrimitive()) {

                String channelKey = entry.getKey();
                String channelValue = entry.getValue().getAsString();
                logger.debug("ThingUID: {} - channel: {}, value: {}", thingId, channelKey, channelValue);

                if (getThing().getChannel(channelKey) != null) {

                    Channel channel = getThing().getChannel(channelKey);

                    switch (channel.getAcceptedItemType()) {

                        case ITEM_TYPE_NUMBER:
                            updateState(channel.getUID(), new DecimalType(channelValue));
                            break;

                        case ITEM_TYPE_STRING:
                            triggerChannel(channel.getUID(), channelValue);
                            break;

                        case ITEM_TYPE_CONTACT:
                            updateState(channel.getUID(),
                                    Boolean.parseBoolean(channelValue) ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
                            break;

                        case ITEM_TYPE_SWITCH:
                            updateState(channel.getUID(),
                                    Boolean.parseBoolean(channelValue) ? OnOffType.ON : OnOffType.OFF);
                            break;

                        default:
                            channelValue = entry.getValue().getAsString();
                            logger.warn("ThingUID: {} - channel not found -> channel: {}", thingId, channel);
                            break;
                    }
                } else {
                    logger.debug("ThingUID: {} - channel '{}' for device not found", thingId, channelKey);
                }
            }
        }

    }

}
