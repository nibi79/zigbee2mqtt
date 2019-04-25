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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.eclipse.smarthome.io.transport.mqtt.MqttConnectionState;
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

    /**
     * Maximum time to search for server in seconds.
     */
    private static final int SEARCH_TIME = 10;

    private String currentIp = null;

    /**
     *
     */
    public Zigbee2MqttBridgeDiscoveryService() {
        super(SUPPORTED_BRIDGE_TYPES, SEARCH_TIME);

    }

    /**
     * Public method for triggering server discovery
     */
    public void discover() {
        startScan();

    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return SUPPORTED_BRIDGE_TYPES;
    }

    @Override
    protected void startBackgroundDiscovery() {
        startScan();
    }

    @Override
    protected void startScan() {

        String subnet = getSubnet();
        if (subnet != null) {

            // Polling all potential IPs of this subnet
            for (int ip = 0; ip <= 255; ip++) {

                currentIp = subnet + String.valueOf(ip);

                if (isLocalIpAddress(currentIp)) {
                    currentIp = "localhost";
                }

                logger.trace("Polling {} ", currentIp);

                if (pingHost(currentIp, 1883, 50)) {

                    proveZ2MMqttBroker();
                }

            }

        } else {
            logger.info("Automatic discovery fails: no LAN subnet found");
        }
    }

    /**
     *
     */
    private void proveZ2MMqttBroker() {

        MqttBrokerConnection mqttBrokerConnection = new MqttBrokerConnection(currentIp, Integer.valueOf(1883), false,
                CLIENTIDPRAEFIX + "discovery");

        mqttBrokerConnection.start();

        while (MqttConnectionState.CONNECTING.equals(mqttBrokerConnection.connectionState())) {

            logger.debug("try to connect to MQTT broker: {}", currentIp);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        if (MqttConnectionState.CONNECTED.equals(mqttBrokerConnection.connectionState())) {

            logger.debug("connected to MQTT broker: {}", currentIp);

            mqttBrokerConnection.subscribe(new Zigbee2MqttTopicHandler().getTopicBridgeConfigDevices(), this);
            mqttBrokerConnection.publish(new Zigbee2MqttTopicHandler().getTopicBridgeConfigDevicesGet(),
                    "get".getBytes());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        mqttBrokerConnection.stop();
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
                        .withProperty(MQTTBROKER_IPADDRESS, currentIp).withLabel("Z2M Server - " + ieeeAddr).build();

                thingDiscovered(discoveryResult);
            }
        }

    }

    /**
     * Fast pinging of a subnet
     *
     * @see https://stackoverflow.com/questions/3584210/preferred-java-way-to-ping-an-http-url-for-availability
     * @param host    Host to ping
     * @param port    Port to ping
     * @param timeout Timeout in milliseconds
     * @return Ping result
     */
    private boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    /**
     * Scans all network adapters and takes the first occurrence of 192.0.0.0 or 10.0.0.0 subnet
     *
     * @return Subnet as String
     */
    private String getSubnet() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    byte[] ip = ia.getAddress();
                    if (ip.length == 4) {// IPv4 support only
                        if (ip[0] == (byte) 192 || ip[1] == 10) {
                            String subnet = String.format("%d.%d.%d.", 0xff & ip[0], 0xff & ip[1], 0xff & ip[2]);
                            return subnet;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // discovery fails
        }
        return null;
    }

    /**
     * Check if given IP is localhost.
     *
     * @param ip
     * @return
     */
    private boolean isLocalIpAddress(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            // Check if the address is a valid special local or loop back
            if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()) {
                return true;
            }

            // Check if the address is defined on any interface
            return NetworkInterface.getByInetAddress(addr) != null;
        } catch (SocketException | UnknownHostException e) {
            return false;
        }
    }

}
