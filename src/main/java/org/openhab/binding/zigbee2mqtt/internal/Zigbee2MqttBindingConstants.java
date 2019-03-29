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

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;

/**
 * The {@link Zigbee2MqttBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Nils
 */
@NonNullByDefault
public class Zigbee2MqttBindingConstants {

    public static final String BINDING_ID = "zigbee2mqtt";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_GATEWAY = new ThingTypeUID(BINDING_ID, "zigbee2mqttServer");
    public static final ThingTypeUID THING_TYPE_DEVICE = new ThingTypeUID(BINDING_ID, "zigbee2mqttDevice");

    /* List of all config properties */
    // TODO
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    public static final Set<ThingTypeUID> SUPPORTED_BRIDGE_TYPES = Collections.singleton(THING_TYPE_GATEWAY);
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections
            .unmodifiableSet(Stream.of(THING_TYPE_GATEWAY, THING_TYPE_DEVICE).collect(Collectors.toSet()));

    // List of all Channel ids
    public static final String CHANNEL_NAME_LOGLEVEL = "logLevel";

    public static final String CHANNEL_NAME_PERMITJOIN = "permitJoin";
    public static final String ITEM_TYPE_COLOR = "Color";
    public static final String ITEM_TYPE_CONTACT = "Contact";
    public static final String ITEM_TYPE_DIMMER = "Dimmer";
    public static final String ITEM_TYPE_NUMBER = "Number";
    public static final String ITEM_TYPE_SWITCH = "Switch";
    public static final String ITEM_TYPE_STRING = "String";

    // config channel binary_sensor
    public static final String CHANNEL_NAME_OCCUPANCY = "occupancy";
    public static final String CHANNEL_LABEL_OCCUPANCY = "Occupancy";
    public static final ChannelTypeUID CHANNEL_OCCUPANCY = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_OCCUPANCY);

    public static final String CHANNEL_NAME_PRESENCE = "presence";
    public static final String CHANNEL_LABEL_PRESENCE = "Presence";
    public static final ChannelTypeUID CHANNEL_PRESENCE = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_PRESENCE);

    public static final String CHANNEL_NAME_CONTACT = "contact";
    public static final String CHANNEL_LABEL_CONTACT = "Contact";
    public static final ChannelTypeUID CHANNEL_CONTACT = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_CONTACT);

    public static final String CHANNEL_NAME_WATER_LEAK = "water_leak";
    public static final String CHANNEL_LABEL_WATER_LEAK = "Waterleak";
    public static final ChannelTypeUID CHANNEL_WATER_LEAK = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_WATER_LEAK);

    public static final String CHANNEL_NAME_SMOKE = "smoke";
    public static final String CHANNEL_LABEL_SMOKE = "Smoke";
    public static final ChannelTypeUID CHANNEL_SMOKE = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_SMOKE);

    public static final String CHANNEL_NAME_GAS = "gas";
    public static final String CHANNEL_LABEL_GAS = "Gas";
    public static final ChannelTypeUID CHANNEL_GAS = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_GAS);

    public static final String CHANNEL_NAME_ROUTER = "router";
    public static final String CHANNEL_LABEL_ROUTER = "Router";
    public static final ChannelTypeUID CHANNEL_ROUTER = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_ROUTER);

    public static final String CHANNEL_NAME_BATTERY_LOW = "battery_low";
    public static final String CHANNEL_LABEL_BATTERY_LOW = "Battery low";
    public static final ChannelTypeUID CHANNEL_BATTERY_LOW = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_BATTERY_LOW);

    // config channel senors
    public static final String CHANNEL_NAME_ILLUMINANCE = "illuminance";
    public static final String CHANNEL_LABEL_ILLUMINANCE = "Illuminance";
    public static final ChannelTypeUID CHANNEL_ILLUMINANCE = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_ILLUMINANCE);

    public static final String CHANNEL_NAME_HUMIDITY = "humidity";
    public static final String CHANNEL_LABEL_HUMIDITY = "Humidity";
    public static final ChannelTypeUID CHANNEL_HUMIDITY = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_HUMIDITY);

    public static final String CHANNEL_NAME_TEMPERATURE = "temperature";
    public static final String CHANNEL_LABEL_TEMPERATURE = "Temperature";
    public static final ChannelTypeUID CHANNEL_TEMPERATURE = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_TEMPERATURE);

    public static final String CHANNEL_NAME_PRESSURE = "pressure";
    public static final String CHANNEL_LABEL_PRESSURE = "Pressure";
    public static final ChannelTypeUID CHANNEL_PRESSURE = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_PRESSURE);

    public static final String CHANNEL_NAME_CLICK = "click";
    public static final String CHANNEL_LABEL_CLICK = "Click";
    public static final ChannelTypeUID CHANNEL_CLICK = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_CLICK);

    public static final String CHANNEL_NAME_POWER = "power";
    public static final String CHANNEL_LABEL_POWER = "Power";
    public static final ChannelTypeUID CHANNEL_POWER = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_POWER);

    public static final String CHANNEL_NAME_ACTION = "action";
    public static final String CHANNEL_LABEL_ACTION = "Action";
    public static final ChannelTypeUID CHANNEL_ACTION = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_ACTION);

    public static final String CHANNEL_NAME_BRIGHTNESS = "brightness";
    public static final String CHANNEL_LABEL_BRIGHTNESS = "Brightness";
    public static final ChannelTypeUID CHANNEL_BRIGHTNESS = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_BRIGHTNESS);

    public static final String CHANNEL_NAME_LOCK = "lock";
    public static final String CHANNEL_LABEL_LOCK = "Lock";
    public static final ChannelTypeUID CHANNEL_LOCK = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_LOCK);

    public static final String CHANNEL_NAME_POWER_BATTERY = "battery";
    public static final String CHANNEL_LABEL_POWER_BATTERY = "Battery";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERY = new ChannelTypeUID(BINDING_ID,
            CHANNEL_NAME_POWER_BATTERY);

    public static final String CHANNEL_NAME_LINKQUALITY = "linkquality";
    public static final String CHANNEL_LABEL_LINKQUALITY = "Linkquality";
    public static final ChannelTypeUID CHANNEL_LINKQUALITY = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_LINKQUALITY);

    public static final String CHANNEL_NAME_GAS_DENSITY = "gas_density";
    public static final String CHANNEL_LABEL_GAS_DENSITY = "Gas density";
    public static final ChannelTypeUID CHANNEL_GAS_DENSITY = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_GAS_DENSITY);

    public static final String CHANNEL_NAME_COVER = "cover";
    public static final String CHANNEL_LABEL_COVER = "Cover";
    public static final ChannelTypeUID CHANNEL_COVER = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_COVER);

    public static final String CHANNEL_NAME_STATE = "state";
    public static final String CHANNEL_LABEL_STATE = "State";
    public static final ChannelTypeUID CHANNEL_STATE = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_STATE);

    public static final String CHANNEL_NAME_SWITCH = "switch";
    public static final String CHANNEL_LABEL_SWITCH = "Switch";
    public static final ChannelTypeUID CHANNEL_SWITCH = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_SWITCH);

    public static final String CHANNEL_NAME_COLORTEMP = "color_temp";
    public static final String CHANNEL_LABEL_COLORTEMP = "Colortemp";
    public static final ChannelTypeUID CHANNEL_COLORTEMP = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_COLORTEMP);

    public static final String CHANNEL_NAME_COLOR = "color";
    public static final String CHANNEL_LABEL_COLOR = "Color";
    public static final ChannelTypeUID CHANNEL_COLOR = new ChannelTypeUID(BINDING_ID, CHANNEL_NAME_COLOR);

    // pseudo channels
    public static final String CHANNEL_NAME_XY = "xy";

}
