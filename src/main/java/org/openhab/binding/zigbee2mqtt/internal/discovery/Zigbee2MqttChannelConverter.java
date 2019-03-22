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

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
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
    public List<Channel> convert(List<ChannelDiscovery> channelDiscoveries, Thing thing) {

        List<Channel> channels = new ArrayList<>();

        for (ChannelDiscovery channelDiscovery : channelDiscoveries) {

            Channel channel = createChannel(channelDiscovery.getId(), channelDiscovery.getConfig(), thing.getUID());
            if (channel != null) {
                channels.add(channel);
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
    private Channel createChannel(String channelId, Map<String, Object> config, ThingUID thingUID) {

        Channel newChannel = null;

        switch (channelId) {

            case CHANNEL_NAME_TEMPERATURE_VALUE:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_TEMPERATURE_VALUE,
                        CHANNEL_LABEL_TEMPERATURE_VALUE);
                break;

            case CHANNEL_NAME_HUMIDITY_VALUE:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_HUMIDITY_VALUE,
                        CHANNEL_LABEL_HUMIDITY_VALUE);
                break;

            case CHANNEL_NAME_ILLUMINANCE_VALUE:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_ILLUMINANCE_VALUE,
                        CHANNEL_LABEL_ILLUMINANCE_VALUE);
                break;

            case CHANNEL_NAME_LINKQUALITY:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_LINKQUALITY,
                        CHANNEL_LABEL_LINKQUALITY);
                break;

            case CHANNEL_NAME_OCCUPANCY_SENSOR:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_OCCUPANCY_SENSOR,
                        CHANNEL_LABEL_OCCUPANCY_SENSOR);
                break;

            case CHANNEL_NAME_POWER_BATTERY:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_POWER_BATTERY,
                        CHANNEL_LABEL_POWER_BATTERY);
                break;

            case CHANNEL_NAME_PRESSURE_VALUE:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_NUMBER, CHANNEL_PRESSURE_VALUE,
                        CHANNEL_LABEL_PRESSURE_VALUE);
                break;

            case CHANNEL_NAME_ACTION:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_ACTION,
                        CHANNEL_LABEL_ACTION);
                break;

            case CHANNEL_NAME_CLICK:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_STRING, CHANNEL_CLICK,
                        CHANNEL_LABEL_CLICK);
                break;

            case CHANNEL_NAME_WATER_LEAK:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_WATER_LEAK,
                        CHANNEL_LABEL_WATER_LEAK);
                break;

            case CHANNEL_NAME_CONTACT:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_CONTACT, CHANNEL_CONTACT,
                        CHANNEL_LABEL_CONTACT);
                break;

            case CHANNEL_NAME_STATE:
                newChannel = createChannel(channelId, thingUID, config, ITEM_TYPE_SWITCH, CHANNEL_STATE,
                        CHANNEL_LABEL_STATE);
                break;

            default:
                logger.warn("no mapping for creating chhannel  for channelId '{}'", channelId);
                break;
        }

        return newChannel;
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
            ChannelTypeUID channelTypeUID, String channelLabel) {

        ChannelUID channelUID = new ChannelUID(thingUID, channelId);

        Channel channel = ChannelBuilder.create(channelUID, itemType).withConfiguration(new Configuration(config))
                .withType(channelTypeUID).withLabel(channelLabel).build();

        return channel;
    }

}
