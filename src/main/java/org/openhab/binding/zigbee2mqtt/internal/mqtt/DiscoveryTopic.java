package org.openhab.binding.zigbee2mqtt.internal.mqtt;

/**
 * The {@link DiscoveryTopic} class...TODO
 *
 * @author Nils
 *
 */
public class DiscoveryTopic {

    private String topic = null;
    private String ieeeAddr = null;
    private String objectId = null;
    private String type = null;

    public DiscoveryTopic(String topic) {

        super();
        this.topic = topic;

        // example: homeassistant/sensor/0x00158d0002320b4f/battery/config
        String[] topicParts = topic.split("/");
        this.ieeeAddr = topicParts[2];
        this.objectId = topicParts[3];
        this.type = topicParts[1];
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

    public String getObjectId() {
        return objectId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TopicHomeassistant [ieeeAddr=" + getIeeeAddr() + ", topic=" + getTopic() + ", objectId=" + objectId
                + ", type=" + type + "]";
    }
}
