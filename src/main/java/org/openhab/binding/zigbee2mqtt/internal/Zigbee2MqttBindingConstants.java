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
    public static final String CHANNEL_NAME_NETWORKMAP = "networkMap";

    public static final String ITEM_TYPE_COLOR = "Color";
    public static final String ITEM_TYPE_CONTACT = "Contact";
    public static final String ITEM_TYPE_DIMMER = "Dimmer";
    public static final String ITEM_TYPE_NUMBER = "Number";
    public static final String ITEM_TYPE_SWITCH = "Switch";
    public static final String ITEM_TYPE_STRING = "String";

    // config channel binary_sensor
    public static final String CHANNEL_NAME_OCCUPANCY = "occupancy";
    public static final String CHANNEL_NAME_PRESENCE = "presence";
    public static final String CHANNEL_NAME_CONTACT = "contact";
    public static final String CHANNEL_NAME_WATER_LEAK = "water_leak";
    public static final String CHANNEL_NAME_SMOKE = "smoke";
    public static final String CHANNEL_NAME_GAS = "gas";
    public static final String CHANNEL_NAME_CARBON_MONOXIDE = "carbon_monoxide";
    public static final String CHANNEL_NAME_ROUTER = "router";
    public static final String CHANNEL_NAME_BATTERY_LOW = "battery_low";
    // config channel senors
    public static final String CHANNEL_NAME_ILLUMINANCE = "illuminance";
    public static final String CHANNEL_NAME_HUMIDITY = "humidity";
    public static final String CHANNEL_NAME_TEMPERATURE = "temperature";
    public static final String CHANNEL_NAME_PRESSURE = "pressure";
    public static final String CHANNEL_NAME_CLICK = "click";
    public static final String CHANNEL_NAME_POWER = "power";
    public static final String CHANNEL_NAME_ACTION = "action";
    public static final String CHANNEL_NAME_BRIGHTNESS = "brightness";
    public static final String CHANNEL_NAME_LOCK = "lock";
    public static final String CHANNEL_NAME_BATTERY = "battery";
    public static final String CHANNEL_NAME_LINKQUALITY = "linkquality";
    public static final String CHANNEL_NAME_GAS_DENSITY = "gas_density";
    public static final String CHANNEL_NAME_COVER = "cover";
    public static final String CHANNEL_NAME_STATE = "state";
    public static final String CHANNEL_NAME_SWITCH = "switch";
    public static final String CHANNEL_NAME_COLORTEMP = "color_temp";
    public static final String CHANNEL_NAME_COLOR = "color";
    // pseudo channels
    public static final String CHANNEL_NAME_XY = "xy";

}
