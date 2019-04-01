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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
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
 * @author Nils
 */
@NonNullByDefault
public class Zigbee2MqttDeviceHandler extends BaseThingHandler implements Zigbee2MqttMessageSubscriber {

    private static final BigDecimal VALUE_154 = BigDecimal.valueOf(154f);

    private static final BigDecimal VALUE_3_46 = BigDecimal.valueOf(3.46f);

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDeviceHandler.class);

    private static final int ROUNDING_PRECISION = 0;
    private static final BigDecimal P100 = new BigDecimal(100);
    private static final BigDecimal BRIGHTNESS_MAX = new BigDecimal(255);

    public Zigbee2MqttDeviceHandler(Thing thing) {
        super(thing);
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
    public void handleCommand(ChannelUID channelUID, Command command) {

        String commandvalue = command.toString();

        Zigbee2MqttBridgeHandler bridgeHandler = getZigbee2MqttBridgeHandler();

        if (command instanceof RefreshType) {
            return;
        }

        Channel channel = getThing().getChannel(channelUID.getId());

        if (channel.getConfiguration().get("command_topic") != null) {

            logger.debug("channel has no command topic!");
            return;
        }

        String commandTopic = (String) channel.getConfiguration().get("command_topic");

        switch (channelUID.getId()) {

            // TODO use converters for sending commands

            case CHANNEL_NAME_STATE:
                String sMsg = createSetMessage(CHANNEL_NAME_STATE, String.valueOf(commandvalue));
                bridgeHandler.getMqttBrokerConnection().publish(commandTopic, sMsg.getBytes());
                break;

            case CHANNEL_NAME_BRIGHTNESS:
                String bMsg = createSetMessage(CHANNEL_NAME_BRIGHTNESS + "_percent", String.valueOf(commandvalue));
                bridgeHandler.getMqttBrokerConnection().publish(commandTopic, bMsg.getBytes());
                break;

            case CHANNEL_NAME_COLORTEMP:
                String cMsg = createSetMessage(CHANNEL_NAME_COLORTEMP + "_percent", String.valueOf(commandvalue));
                bridgeHandler.getMqttBrokerConnection().publish(commandTopic, cMsg.getBytes());
                break;

            case CHANNEL_NAME_COLOR:
                String commandMsg = null;
                if (command instanceof HSBType) {

                    HSBType hsb = (HSBType) command;

                    // xy
                    PercentType[] xy = hsb.toXY();
                    float x = Float.valueOf(xy[0].floatValue() / 100.0f);
                    float y = Float.valueOf(xy[1].floatValue() / 100.0f);

                    commandMsg = "{\"brightness_percent\": \"" + String.valueOf(hsb.getBrightness().toString()) + "\","
                            + "\"color\": {" + "\"x\": " + String.valueOf(x) + "," + "\"y\": " + String.valueOf(y) + ""
                            + "}}";

                } else if (command instanceof PercentType) {

                    commandMsg = createSetMessage(CHANNEL_NAME_BRIGHTNESS + "_percent", String.valueOf(commandvalue));
                }

                bridgeHandler.getMqttBrokerConnection().publish(commandTopic, commandMsg.getBytes());
                break;

            // TODO implement command channel: cover, lock

            default:
                logger.debug("command for ChannelUID not supported: {}", channelUID.getAsString());
                break;
        }

    }

    /**
     * @param key
     * @param value
     * @return
     */
    private String createSetMessage(String key, String value) {

        return "{\"" + key + "\": \"" + value + "\"}";
    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        String thingId = getThing().getUID().getId();
        logger.debug("incoming message for ThingUID: {}", thingId);

        for (Entry<String, JsonElement> entry : jsonMessage.entrySet()) {

            String channelKey = entry.getKey();
            JsonElement channelValue = entry.getValue();
            logger.debug("ThingUID: {} - channel: {}, value: {}", thingId, channelKey, channelValue);

            if (getThing().getChannel(channelKey) != null) {

                Channel channel = getThing().getChannel(channelKey);

                if (channel != null) {

                    switch (channelKey) {

                        // TODO use converters for reading states

                        // type sensors
                        // TODO implement sensors channels: power, lock, gas_density, cover
                        case CHANNEL_NAME_ILLUMINANCE:
                        case CHANNEL_NAME_HUMIDITY:
                        case CHANNEL_NAME_TEMPERATURE:
                        case CHANNEL_NAME_PRESSURE:
                        case CHANNEL_NAME_LINKQUALITY:
                        case CHANNEL_NAME_POWER_BATTERY:
                            updateState(channel.getUID(), new DecimalType(channelValue.getAsString()));
                            break;

                        case CHANNEL_NAME_COLORTEMP:
                            BigDecimal cValue = new BigDecimal(channelValue.getAsString()).subtract(VALUE_154)
                                    .divide(VALUE_3_46, ROUNDING_PRECISION, RoundingMode.HALF_UP);
                            updateState(channel.getUID(), new PercentType(cValue));
                            break;

                        case CHANNEL_NAME_BRIGHTNESS:
                            BigDecimal bValue = getPercentValue(BRIGHTNESS_MAX, channelValue.getAsString());
                            updateState(channel.getUID(), new PercentType(bValue.round(new MathContext(2))));
                            break;

                        case CHANNEL_NAME_ACTION:
                        case CHANNEL_NAME_CLICK:
                            triggerChannel(channel.getUID(), channelValue.getAsString());
                            break;

                        // type binary-sensors
                        // TODO implement binary-sensors channels: presence, smoke, gas, router, battery_low
                        // TODO use payload_on / payload_of for binary-sensors
                        case CHANNEL_NAME_WATER_LEAK:
                        case CHANNEL_NAME_OCCUPANCY:
                            updateState(channel.getUID(), channelValue.getAsBoolean() ? OnOffType.ON : OnOffType.OFF);
                            break;

                        case CHANNEL_NAME_CONTACT:
                            updateState(channel.getUID(),
                                    Boolean.parseBoolean(channelValue.getAsString()) ? OpenClosedType.CLOSED
                                            : OpenClosedType.OPEN);
                            break;

                        case CHANNEL_NAME_STATE:
                            updateState(channel.getUID(), OnOffType.valueOf(channelValue.getAsString()));
                            break;

                        // type light
                        case CHANNEL_NAME_COLOR:
                            float x = channelValue.getAsJsonObject().get("x").getAsFloat();
                            float y = channelValue.getAsJsonObject().get("y").getAsFloat();

                            HSBType hsb = HSBType.fromXY(x, y);
                            updateState(channel.getUID(), hsb);

                            // brightness was set to 100% by method 'fromXY'
                            String brightness = jsonMessage.get("brightness").getAsString();
                            BigDecimal b = getPercentValue(BRIGHTNESS_MAX, brightness);
                            updateState(channel.getUID(), new PercentType(b.round(new MathContext(2))));

                            break;

                        default:
                            logger.warn("ThingUID: {} - channel not found -> channel: {}", thingId, channel);
                            break;
                    }
                }
            } else {
                logger.debug("ThingUID: {} - channel '{}' for device not found", thingId, channelKey);
            }
        }
    }

    /**
     * @param maxValue
     * @param absoluteValue
     * @return
     */
    private BigDecimal getPercentValue(BigDecimal maxValue, String absoluteValue) {

        BigDecimal bValue = new BigDecimal(absoluteValue).multiply(P100).divide(maxValue, ROUNDING_PRECISION,
                RoundingMode.HALF_UP);
        return bValue;
    }

}
