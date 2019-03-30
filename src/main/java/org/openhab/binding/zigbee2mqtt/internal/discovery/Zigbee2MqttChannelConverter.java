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

                        // brightness
                        if (Boolean.valueOf(config.get(CHANNEL_NAME_BRIGHTNESS) != null
                                ? config.get(CHANNEL_NAME_BRIGHTNESS).toString()
                                : null)) {

                            channels.add(createChannel(CHANNEL_NAME_BRIGHTNESS, config, thing.getUID()));
                        }
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

        switch (channelId) {

            // channel binary_sensor
            case CHANNEL_NAME_OCCUPANCY:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_OCCUPANCY,
                        ChannelKind.STATE, CHANNEL_LABEL_OCCUPANCY);

            case CHANNEL_NAME_PRESENCE:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_PRESENCE, ChannelKind.STATE,
                        CHANNEL_LABEL_PRESENCE);

            case CHANNEL_NAME_CONTACT:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_CONTACT, CHANNEL_CONTACT, ChannelKind.STATE,
                        CHANNEL_LABEL_CONTACT);

            case CHANNEL_NAME_WATER_LEAK:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_WATER_LEAK,
                        ChannelKind.STATE, CHANNEL_LABEL_WATER_LEAK);

            case CHANNEL_NAME_SMOKE:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_SMOKE, ChannelKind.STATE,
                        CHANNEL_LABEL_SMOKE);

            case CHANNEL_NAME_GAS:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_GAS, ChannelKind.STATE,
                        CHANNEL_LABEL_GAS);

            case CHANNEL_NAME_ROUTER:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_ROUTER, ChannelKind.STATE,
                        CHANNEL_LABEL_ROUTER);

            case CHANNEL_NAME_BATTERY_LOW:
                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_BATTERY_LOW,
                        ChannelKind.STATE, CHANNEL_LABEL_BATTERY_LOW);

            // channel sensor
            case CHANNEL_NAME_ILLUMINANCE:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_ILLUMINANCE,
                        ChannelKind.STATE, CHANNEL_LABEL_ILLUMINANCE);

            case CHANNEL_NAME_HUMIDITY:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_HUMIDITY, ChannelKind.STATE,
                        CHANNEL_LABEL_HUMIDITY);

            case CHANNEL_NAME_TEMPERATURE:
                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_TEMPERATURE,
                        ChannelKind.STATE, CHANNEL_LABEL_TEMPERATURE);

            case CHANNEL_NAME_PRESSURE:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_PRESSURE, ChannelKind.STATE,
                        CHANNEL_LABEL_PRESSURE);

            case CHANNEL_NAME_CLICK:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_CLICK, ChannelKind.TRIGGER,
                        CHANNEL_LABEL_CLICK);

            case CHANNEL_NAME_POWER:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_POWER, ChannelKind.TRIGGER,
                        CHANNEL_LABEL_POWER);

            case CHANNEL_NAME_ACTION:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_ACTION, ChannelKind.TRIGGER,
                        CHANNEL_LABEL_ACTION);

            case CHANNEL_NAME_LOCK:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_LOCK, ChannelKind.TRIGGER,
                        CHANNEL_LABEL_LOCK);

            case CHANNEL_NAME_POWER_BATTERY:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_POWER_BATTERY,
                        ChannelKind.STATE, CHANNEL_LABEL_POWER_BATTERY);

            case CHANNEL_NAME_LINKQUALITY:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_LINKQUALITY,
                        ChannelKind.STATE, CHANNEL_LABEL_LINKQUALITY);

            case CHANNEL_NAME_GAS_DENSITY:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_GAS_DENSITY,
                        ChannelKind.STATE, CHANNEL_LABEL_GAS_DENSITY);
            case CHANNEL_NAME_COVER:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_COVER, ChannelKind.STATE,
                        CHANNEL_LABEL_COVER);

            case CHANNEL_NAME_STATE:

                return createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_STATE, ChannelKind.STATE,
                        CHANNEL_LABEL_STATE);

            case CHANNEL_NAME_BRIGHTNESS:

                return createChannel(CHANNEL_NAME_BRIGHTNESS, thingUID, config, ITEM_TYPE_DIMMER, CHANNEL_BRIGHTNESS,
                        ChannelKind.STATE, CHANNEL_LABEL_BRIGHTNESS);

            case CHANNEL_NAME_COLORTEMP:

                return createChannel(CHANNEL_NAME_COLORTEMP, thingUID, config, ITEM_TYPE_DIMMER, CHANNEL_COLORTEMP,
                        ChannelKind.STATE, CHANNEL_LABEL_COLORTEMP);

            case CHANNEL_NAME_COLOR:

                return createChannel(CHANNEL_NAME_COLOR, thingUID, config, ITEM_TYPE_COLOR, CHANNEL_COLORTEMP,
                        ChannelKind.STATE, CHANNEL_LABEL_COLOR);

            default:
                logger.warn("no mapping for creating chhannel  for channelId '{}'", channelId);
                return null;
        }

    }

    /**
     * Build the channel.
     *
     * @param channelId
     * @param thingUID
     * @param config
     * @param itemType
     * @param channelTypeUID
     * @param channelLabel
     * @return
     */
    private Channel createChannel(String channelId, ThingUID thingUID, Map<String, Object> config, String itemType,
            ChannelTypeUID channelTypeUID, ChannelKind channelKind, String channelLabel) {

        ChannelUID channelUID = new ChannelUID(thingUID, channelId);

        Channel channel = ChannelBuilder.create(channelUID, itemType).withConfiguration(new Configuration(config))
                .withType(channelTypeUID).withLabel(channelLabel).withKind(channelKind).build();

        return channel;
    }

}
