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

import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionObserver;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionState;
import org.openhab.binding.zigbee2mqtt.internal.discovery.Zigbee2MqttDiscoveryService;
import org.openhab.binding.zigbee2mqtt.internal.mqtt.Zigbee2MqttMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * The {@link Zigbee2MqttBridgeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Nils
 */
public class Zigbee2MqttBridgeHandler extends BaseBridgeHandler
        implements Zigbee2MqttMessageSubscriber, MqttConnectionObserver {

    private static final byte[] HEARTBEAT = "heartbeat".getBytes();

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttBridgeHandler.class);

    private MqttBrokerConnection mqttBrokerConnection;

    @SuppressWarnings("unused")
    private @Nullable Zigbee2MqttDiscoveryService discoveryService;

    @NonNull
    private Zigbee2MqttBridgeConfiguration config = new Zigbee2MqttBridgeConfiguration();

    public Zigbee2MqttBridgeHandler(Bridge thing) {
        super(thing);
    }

    @Override
    public void initialize() {

        try {

            config = getConfigAs(Zigbee2MqttBridgeConfiguration.class);

            mqttBrokerConnection = createBrokerConnection(config);
            mqttBrokerConnection.addConnectionObserver(this);

            if (!mqttBrokerConnection.start().get().booleanValue()) {

                logger.error("Cannot connect to broker: {}", config.toString());
                updateStatus(ThingStatus.OFFLINE);
            }

        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
            updateStatus(ThingStatus.OFFLINE);
        }

    }

    public void setDiscoveryService(Zigbee2MqttDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Override
    public void dispose() {

        try {
            mqttBrokerConnection.stop().get().booleanValue();

            super.dispose();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
            updateStatus(ThingStatus.OFFLINE);
        }
    }

    /**
     * Creates a broker connection based on the configuration of {@link #config}.
     *
     * @param config
     *
     * @return Returns a valid MqttBrokerConnection
     * @throws IllegalArgumentException If the configuration is invalid, this exception is thrown.
     */
    protected MqttBrokerConnection createBrokerConnection(@NonNull Zigbee2MqttBridgeConfiguration config)
            throws IllegalArgumentException {

        String host = config.getMqttbrokerIpAddress();
        if (StringUtils.isBlank(host) || host == null) {
            throw new IllegalArgumentException("MqttbrokerIpAddress is empty!");
        }

        MqttBrokerConnection c = new MqttBrokerConnection(host, config.getMqttbrokerPort(), false,
                config.getMqttbrokerClientID());

        String username = config.getMqttbrokerUsername();
        String password = config.getMqttbrokerPassword();
        if (StringUtils.isNotBlank(username) && password != null) {
            c.setCredentials(username, password); // Empty passwords are allowed
        }

        return c;
    }

    @Override
    public void updateStatus(ThingStatus status) {
        super.updateStatus(status);

        if (ThingStatus.ONLINE.equals(status)) {

            discoveryService.discover();
        }
    }

    /**
     * @return
     */
    public MqttBrokerConnection getMqttBrokerConnection() {
        return mqttBrokerConnection;
    }

    public void setDiscovery(Zigbee2MqttDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        if (command instanceof RefreshType) {
            return;
        }

        switch (channelUID.getId()) {
            case CHANNEL_NAME_PERMITJOIN:
                String permitjoin = OnOffType.ON.toString().equals(command.toString()) ? "true" : "false";
                mqttBrokerConnection.publish(getMqttbrokerBaseTopic() + "/bridge/config/permit_join",
                        permitjoin.getBytes());
                break;

            case CHANNEL_NAME_LOGLEVEL:
                String loglevel = command.toString();
                mqttBrokerConnection.publish(getMqttbrokerBaseTopic() + "/bridge/config/log_level",
                        loglevel.getBytes());
                break;

            default:
                break;
        }

        logger.debug("command for ChannelUID not supported: {}", channelUID.getAsString());
    }

    @Override
    public void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage) {

        String action = topic.replaceFirst(config.getMqttbrokerBaseTopic() + "/bridge/", "");

        switch (action) {
            case "state":
                ThingStatus status = ThingStatus.valueOf(jsonMessage.get("message").getAsString().toUpperCase());
                updateStatus(status);
                break;
            case "config":
                String logLevel = jsonMessage.get("log_level").getAsString();
                Channel channelLogLevel = getThing().getChannel(CHANNEL_NAME_LOGLEVEL);
                if (channelLogLevel != null) {
                    updateState(channelLogLevel.getUID(), StringType.valueOf(logLevel));
                }

                String permitJoin = jsonMessage.get("permit_join").getAsString();
                Channel channelPermitJoin = getThing().getChannel(CHANNEL_NAME_PERMITJOIN);
                if (channelPermitJoin != null) {
                    updateState(channelPermitJoin.getUID(),
                            Boolean.parseBoolean(permitJoin) ? OnOffType.ON : OnOffType.OFF);
                }

                break;
            case "log":
                String type = jsonMessage.get("type").getAsString();

                switch (type) {
                    case "pairing":
                        // TODO
                        logger.info(jsonMessage.toString());
                        break;
                    case "device_connected":
                        logger.info(jsonMessage.toString());
                        discoveryService.discover();
                        break;
                    case "zigbee_publish_error":

                        logger.error(jsonMessage.toString());
                        break;

                    default:
                        logger.warn(jsonMessage.toString());
                        break;
                }

                break;

            default:
                break;
        }

    }

    /**
     * @return
     */
    public String getMqttbrokerBaseTopic() {

        return config.getMqttbrokerBaseTopic();
    }

    @Override
    public void connectionStateChanged(MqttConnectionState state, @Nullable Throwable error) {

        logger.debug("Broker connection changed to: {}", state.toString());

        switch (state.toString()) {
            case "DISCONNECTED":
                updateStatus(ThingStatus.OFFLINE);
                break;
            case "CONNECTION":
                updateStatus(ThingStatus.UNKNOWN);
                break;
            case "CONNECTED":
                updateStatus(ThingStatus.UNKNOWN);
                mqttBrokerConnection.subscribe(getMqttbrokerBaseTopic() + "/bridge/#", this);
                break;

            default:
                break;
        }
    }

}
