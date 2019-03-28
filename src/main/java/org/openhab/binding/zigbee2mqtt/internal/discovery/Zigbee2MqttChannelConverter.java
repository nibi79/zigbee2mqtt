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

            List<Channel> newChannels = createChannel(channelDiscovery.getObjetcId(), channelDiscovery.getConfig(),
                    thing.getUID());

            if (!newChannels.isEmpty()) {
                channels.addAll(newChannels);
            }
        }

        return channels;

    }

    /**
     * Creates the channel from the given id.
     *
     * @param channelId
     * @param config    key/value
     * @param thingUID
     * @return
     */
    private @NonNull List<Channel> createChannel(String channelId, Map<String, Object> config, ThingUID thingUID) {

        List<Channel> newChannels = new ArrayList<>();

        switch (channelId) {

            // channel binary_sensor
            case CHANNEL_NAME_OCCUPANCY:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_OCCUPANCY_SENSOR,
                        ChannelKind.STATE, CHANNEL_LABEL_OCCUPANCY));
                break;

            case CHANNEL_NAME_PRESENCE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_PRESENCE,
                        ChannelKind.STATE, CHANNEL_LABEL_PRESENCE));
                break;

            case CHANNEL_NAME_CONTACT:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_CONTACT, CHANNEL_CONTACT,
                        ChannelKind.STATE, CHANNEL_LABEL_CONTACT));
                break;

            case CHANNEL_NAME_WATER_LEAK:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_WATER_LEAK,
                        ChannelKind.STATE, CHANNEL_LABEL_WATER_LEAK));
                break;

            case CHANNEL_NAME_SMOKE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_SMOKE,
                        ChannelKind.STATE, CHANNEL_LABEL_SMOKE));
                break;

            case CHANNEL_NAME_GAS:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_GAS,
                        ChannelKind.STATE, CHANNEL_LABEL_GAS));
                break;

            case CHANNEL_NAME_ROUTER:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_ROUTER,
                        ChannelKind.STATE, CHANNEL_LABEL_ROUTER));
                break;

            case CHANNEL_NAME_BATTERY_LOW:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_BATTERY_LOW,
                        ChannelKind.STATE, CHANNEL_LABEL_BATTERY_LOW));
                break;

            // channel sensor
            case CHANNEL_NAME_ILLUMINANCE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_ILLUMINANCE,
                        ChannelKind.STATE, CHANNEL_LABEL_ILLUMINANCE));
                break;

            case CHANNEL_NAME_HUMIDITY_VALUE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_HUMIDITY_VALUE,
                        ChannelKind.STATE, CHANNEL_LABEL_HUMIDITY_VALUE));
                break;
            case CHANNEL_NAME_TEMPERATURE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_TEMPERATURE,
                        ChannelKind.STATE, CHANNEL_LABEL_TEMPERATURE));
                break;

            case CHANNEL_NAME_PRESSURE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_PRESSURE_VALUE,
                        ChannelKind.STATE, CHANNEL_LABEL_PRESSURE));
                break;

            case CHANNEL_NAME_CLICK:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_CLICK,
                        ChannelKind.TRIGGER, CHANNEL_LABEL_CLICK));
                break;

            case CHANNEL_NAME_POWER:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_POWER,
                        ChannelKind.TRIGGER, CHANNEL_LABEL_POWER));
                break;

            case CHANNEL_NAME_ACTION:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_ACTION,
                        ChannelKind.TRIGGER, CHANNEL_LABEL_ACTION));
                break;

            case CHANNEL_NAME_LOCK:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_LOCK,
                        ChannelKind.TRIGGER, CHANNEL_LABEL_LOCK));
                break;

            case CHANNEL_NAME_POWER_BATTERY:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_POWER_BATTERY,
                        ChannelKind.STATE, CHANNEL_LABEL_POWER_BATTERY));
                break;

            case CHANNEL_NAME_LINKQUALITY:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_LINKQUALITY,
                        ChannelKind.STATE, CHANNEL_LABEL_LINKQUALITY));
                break;

            case CHANNEL_NAME_GAS_DENSITY:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_GAS_DENSITY,
                        ChannelKind.STATE, CHANNEL_LABEL_GAS_DENSITY));
                break;
            case CHANNEL_NAME_COVER:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_COVER,
                        ChannelKind.STATE, CHANNEL_LABEL_COVER));
                break;

            case CHANNEL_NAME_STATE:
                newChannels.add(createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_STATE,
                        ChannelKind.STATE, CHANNEL_LABEL_STATE));
                break;

            // Switch is State
            case CHANNEL_NAME_SWITCH:
                newChannels.add(createChannel(CHANNEL_NAME_STATE, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_STATE,
                        ChannelKind.STATE, CHANNEL_LABEL_STATE));
                break;

            // Light is state, brgihtness...
            case CHANNEL_NAME_LIGHT:
                newChannels.add(createChannel(CHANNEL_NAME_STATE, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_STATE,
                        ChannelKind.STATE, CHANNEL_LABEL_STATE));

                if (Boolean.valueOf(config.get(CHANNEL_NAME_BRIGHTNESS).toString())) {
                    newChannels.add(createChannel(CHANNEL_NAME_BRIGHTNESS, thingUID, config, ITEM_TYPE_DIMMER,
                            CHANNEL_BRIGHTNESS, ChannelKind.STATE, CHANNEL_LABEL_BRIGHTNESS));
                }
                if (Boolean.valueOf(config.get(CHANNEL_NAME_COLORTEMP).toString())) {
                    newChannels.add(createChannel(CHANNEL_NAME_COLORTEMP, thingUID, config, ITEM_TYPE_DIMMER,
                            CHANNEL_COLORTEMP, ChannelKind.STATE, CHANNEL_LABEL_COLORTEMP));
                }
                if (Boolean.valueOf(config.get(CHANNEL_NAME_XY).toString())) {
                    newChannels.add(createChannel(CHANNEL_NAME_COLOR, thingUID, config, ITEM_TYPE_COLOR,
                            CHANNEL_COLORTEMP, ChannelKind.STATE, CHANNEL_LABEL_COLOR));
                }
                break;

            default:
                logger.warn("no mapping for creating chhannel  for channelId '{}'", channelId);
                break;
        }

        return newChannels;
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
