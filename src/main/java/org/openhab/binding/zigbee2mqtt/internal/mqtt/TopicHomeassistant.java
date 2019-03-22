package org.openhab.binding.zigbee2mqtt.internal.mqtt;

/**
 * The {@link TopicHomeassistant} class...TODO
 *
 * @author Nils
 *
 */
public class TopicHomeassistant {

    public static final String TOPIC_HOMEASSISTANT = "homeassistant/#";

    private String topic = null;
    private String ieeeAddr = null;
    private String id = null;

    private String category = null;

    public TopicHomeassistant(String topic) {

        super();
        this.topic = topic;

        // example: homeassistant/sensor/0x00158d0002320b4f/battery/config
        String[] topicParts = topic.split("/");
        this.ieeeAddr = topicParts[2];
        this.id = topicParts[3];
        this.category = topicParts[1];
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getIeeeAddr() {
        return ieeeAddr;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}
