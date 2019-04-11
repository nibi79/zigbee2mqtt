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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
import org.eclipse.smarthome.core.thing.type.ChannelKind;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.openhab.binding.zigbee2mqtt.internal.discovery.result.ChannelDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Zigbee2MqttChannelConverter} class...TODO
 *
 * @author Nils
 */
public class Zigbee2MqttChannelConverter {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttChannelConverter.class);

    /**
     * Converts the discovered channels to thing channels.
     *
     * @param channelDicoveries
     * @param thing
     * @return
     */
    public @NonNull List<Channel> convert(List<ChannelDiscovery> channelDiscoveries, Thing thing) {

        List<Channel> channels = new ArrayList<>();

        for (ChannelDiscovery channelDiscovery : channelDiscoveries) {

            Map<String, Object> config = channelDiscovery.getConfig();

            String type = channelDiscovery.getType();
            if (type != null) {

                switch (type) {

                    case "sensor":
                    case "binary_sensor":
                    case "switch":
                        // value_template
                        String valueTemplate = extractValueTemplate(channelDiscovery);
                        if (valueTemplate != null) {

                            Channel newChannel = createChannel(valueTemplate, config, thing.getUID());
                            channels.add(newChannel);
                        }

                        break;

                    case "light":
                        // on/off
                        channels.add(createChannel(CHANNEL_NAME_STATE, config, thing.getUID()));

                        // color_temp
                        if (Boolean.valueOf(config.get(CHANNEL_NAME_COLORTEMP) != null
                                ? config.get(CHANNEL_NAME_COLORTEMP).toString()
                                : null)) {

                            channels.add(createChannel(CHANNEL_NAME_COLORTEMP, config, thing.getUID()));
                        }
                        // xy
                        // TODO review xy -> color?
                        if (Boolean.valueOf(
                                config.get(CHANNEL_NAME_XY) != null ? config.get(CHANNEL_NAME_XY).toString() : null)) {

                            channels.add(createChannel(CHANNEL_NAME_COLOR, config, thing.getUID()));
                            break;
                        } else if (Boolean.valueOf(config.get(CHANNEL_NAME_BRIGHTNESS) != null
                                ? config.get(CHANNEL_NAME_BRIGHTNESS).toString()
                                : null)) {

                            channels.add(createChannel(CHANNEL_NAME_BRIGHTNESS, config, thing.getUID()));
                        }

                        break;

                    default:
                        break;
                }
            }
        }
        return channels;
    }

    /**
     * @param channelDiscovery
     * @return
     */
    private String extractValueTemplate(ChannelDiscovery channelDiscovery) {

        String valueTemplate = String.valueOf(channelDiscovery.getConfig().get("value_template"));

        if (valueTemplate != null) {
            // TODO check to use regex instead
            return valueTemplate.replace("{{ value_json.", "").replace(" }}", "");
        } else {
            return null;
        }
    }

    /**
     * Creates the channel from the given id.
     *
     * @param channelId
     * @param config    key/value
     * @param thingUID
     * @return
     */
    private Channel createChannel(String channelId, Map<String, Object> config, ThingUID thingUID) {

        ChannelUID channelUID = new ChannelUID(thingUID, channelId);
        ChannelTypeUID channelTypeUID = new ChannelTypeUID(BINDING_ID, channelId);

        switch (channelId) {

            // channel binary_sensor
            case CHANNEL_NAME_OCCUPANCY:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_PRESENCE:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_CONTACT:
                return createStateChannel(channelUID, ITEM_TYPE_CONTACT, channelTypeUID, config);

            case CHANNEL_NAME_WATER_LEAK:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_SMOKE:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_GAS:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_ROUTER:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_BATTERY_LOW:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            // channel sensor
            case CHANNEL_NAME_ILLUMINANCE:
                return createStateChannel(channelUID, ITEM_TYPE_NUMBER, channelTypeUID, config);

            case CHANNEL_NAME_HUMIDITY:
                return createStateChannel(channelUID, ITEM_TYPE_NUMBER, channelTypeUID, config);

            case CHANNEL_NAME_TEMPERATURE:
                return createStateChannel(channelUID, ITEM_TYPE_NUMBER, channelTypeUID, config);

            case CHANNEL_NAME_PRESSURE:
                return createStateChannel(channelUID, ITEM_TYPE_NUMBER, channelTypeUID, config);

            case CHANNEL_NAME_CLICK:
                return createTriggerChannel(channelUID, channelTypeUID, config);

            case CHANNEL_NAME_POWER:
                return createTriggerChannel(channelUID, channelTypeUID, config);

            case CHANNEL_NAME_ACTION:
                return createTriggerChannel(channelUID, channelTypeUID, config);

            case CHANNEL_NAME_LOCK:
                return createTriggerChannel(channelUID, channelTypeUID, config);

            case CHANNEL_NAME_POWER_BATTERY:
                return createStateChannel(channelUID, ITEM_TYPE_NUMBER, channelTypeUID, config);

            case CHANNEL_NAME_LINKQUALITY:
                return createStateChannel(channelUID, ITEM_TYPE_NUMBER, channelTypeUID, config);

            case CHANNEL_NAME_GAS_DENSITY:
                return createStateChannel(channelUID, ITEM_TYPE_STRING, channelTypeUID, config);

            case CHANNEL_NAME_COVER:
                return createStateChannel(channelUID, ITEM_TYPE_STRING, channelTypeUID, config);

            case CHANNEL_NAME_STATE:
                return createStateChannel(channelUID, ITEM_TYPE_SWITCH, channelTypeUID, config);

            case CHANNEL_NAME_BRIGHTNESS:
                return createStateChannel(channelUID, ITEM_TYPE_DIMMER, channelTypeUID, config);

            case CHANNEL_NAME_COLORTEMP:
                return createStateChannel(channelUID, ITEM_TYPE_DIMMER, channelTypeUID, config);

            case CHANNEL_NAME_COLOR:
                return createStateChannel(channelUID, ITEM_TYPE_COLOR, channelTypeUID, config);

            default:
                logger.warn("no mapping for creating chhannel  for channelId '{}'", channelId);
                return null;
        }

    }

    /**
     * Build a state channel.
     *
     * @param channelUID
     * @param itemType
     * @param channelTypeUID
     * @param config
     * @return
     */
    private Channel createStateChannel(ChannelUID channelUID, String itemType, ChannelTypeUID channelTypeUID,
            Map<String, Object> config) {

        return createChannel(channelUID, itemType, channelTypeUID, ChannelKind.STATE, config);
    }

    /**
     * Build a trigger channel.
     *
     * @param channelUID
     * @param itemType
     * @param channelTypeUID
     * @param config
     * @return
     */
    private Channel createTriggerChannel(ChannelUID channelUID, ChannelTypeUID channelTypeUID,
            Map<String, Object> config) {

        return createChannel(channelUID, null, channelTypeUID, ChannelKind.TRIGGER, config);
    }

    /**
     * Build the channel.
     *
     * @param channelUID
     * @param itemType
     * @param channelTypeUID
     * @param channelKind
     * @param config
     * @return
     */
    private Channel createChannel(ChannelUID channelUID, String itemType, ChannelTypeUID channelTypeUID,
            ChannelKind channelKind, Map<String, Object> config) {

        Channel channel = ChannelBuilder.create(channelUID, itemType).withType(channelTypeUID).withKind(channelKind)
                .withConfiguration(new Configuration(config)).build();

        return channel;
    }

}
