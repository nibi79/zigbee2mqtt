package org.openhab.binding.zigbee2mqtt.internal.mqtt;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.io.transport.mqtt.MqttMessageSubscriber;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttDeviceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public interface Zigbee2MqttMessageSubscriber extends MqttMessageSubscriber {

    final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDeviceHandler.class);

    @Override
    public default void processMessage(@NonNull String topic, byte @NonNull [] payload) {

        String message = new String(payload);
        logger.debug("incoming message for topic: {} -> {}", topic, message);

        JsonParser parser = new JsonParser();
        JsonObject jsonMessage = parser.parse(message).getAsJsonObject();

        processMessage(topic, jsonMessage);
    }

    /**
     * @param topic
     * @param jsonMessage
     */
    abstract void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage);

}
