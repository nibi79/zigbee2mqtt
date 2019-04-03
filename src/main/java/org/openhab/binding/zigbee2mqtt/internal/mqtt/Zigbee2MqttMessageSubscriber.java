package org.openhab.binding.zigbee2mqtt.internal.mqtt;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.io.transport.mqtt.MqttMessageSubscriber;
import org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttDeviceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link Zigbee2MqttMessageSubscriber} class...TODO
 *
 * @author Nils
 *
 */
public interface Zigbee2MqttMessageSubscriber extends MqttMessageSubscriber {

    final Logger logger = LoggerFactory.getLogger(Zigbee2MqttDeviceHandler.class);

    @Override
    public default void processMessage(@NonNull String topic, byte @NonNull [] payload) {

        String message = new String(payload);
        logger.debug("incoming message for topic: {} -> {}", topic, message);

        JsonParser parser = new JsonParser();
        JsonElement msgJsonElement = null;
        JsonObject msgJsonObject = null;

        try {
            msgJsonElement = parser.parse(message);
        } catch (Exception e) {
            msgJsonElement = stringToJsonElement(message);
        }

        if (!msgJsonElement.isJsonObject()) {
            msgJsonObject = new JsonObject();
            msgJsonObject.add("message", msgJsonElement);
        } else {
            msgJsonObject = msgJsonElement.getAsJsonObject();
        }

        if (!msgJsonObject.isJsonNull()) {
            processMessage(topic, msgJsonObject);
        } else {
            logger.warn("no valid message for topic: {} -> {}", topic, message);
        }
    }

    /**
     * Replaces all special characters and creates a JsonPrimitiv with the formatted jsonString.
     *
     *
     * @param jsonString
     * @return
     */
    default JsonElement stringToJsonElement(@NonNull String jsonString) {
        JsonParser parser = new JsonParser();
        String formatted = jsonString;
        formatted = formatted.replaceAll("\"", "\\\\\"");
        formatted = formatted.replaceAll("\r\n", " ");
        formatted = formatted.replaceAll("\t", " ");
        formatted = "\"" + formatted + "\"";
        return parser.parse(formatted);
    }

    /**
     * @param topic
     * @param jsonMessage
     */
    abstract void processMessage(@NonNull String topic, @NonNull JsonObject jsonMessage);
}
