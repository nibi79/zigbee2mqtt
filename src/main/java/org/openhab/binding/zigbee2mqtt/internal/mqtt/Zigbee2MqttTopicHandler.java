package org.openhab.binding.zigbee2mqtt.internal.mqtt;

public class Zigbee2MqttTopicHandler {

    private String baseTopic = null;
    private String discoveryTopic = null;

    /**
     * Set baseTopic to default name 'zigbee2mqtt'.
     * Set baseTopic to default name 'homeassistant'.
     *
     */
    public Zigbee2MqttTopicHandler() {
        super();
        this.baseTopic = "zigbee2mqtt";
        this.discoveryTopic = "homeassistant";
    }

    /**
     * @param baseTopic
     */
    public Zigbee2MqttTopicHandler(String baseTopic, String discoveryTopic) {
        super();
        this.baseTopic = baseTopic;
        this.discoveryTopic = discoveryTopic;
    }

    /**
     * @return the discoveryTopic
     */
    public String getDiscoveryTopic() {
        return discoveryTopic;
    }

    /**
     * @param discoverTopic the discoveryTopic to set
     */
    public void setDiscoveryTopic(String discoveryTopic) {
        this.discoveryTopic = discoveryTopic;
    }

    /**
     * @return baseTopic
     */
    public String getBaseTopic() {
        return baseTopic;
    }

    /**
     * Set the baseTopic name.
     *
     * @param baseTopic
     */
    public void setBaseTopic(String baseTopic) {
        this.baseTopic = baseTopic;
    }

    /**
     * Ectract action from topic name.
     *
     * example: 'zigbee2mqtt/bridge/state' -> 'state'
     *
     * @param topic
     * @return
     */
    public String getActionFromTopic(String topic) {

        return topic.replaceFirst(baseTopic.concat("/bridge/"), "");
    }

    /**
     * @return {@literal <baseTopic>/bridge/state}
     */
    public String getTopicBridgeState() {

        return baseTopic.concat("/bridge/state");
    }

    /**
     * @return {@literal <baseTopic>/bridge/networkmap}
     */
    public String getTopicBridgeNetworkmap() {

        return baseTopic.concat("/bridge/networkmap");
    }

    /**
     * @return {@literal <baseTopic>/bridge/networkmap/graphviz}
     */
    public String getTopicBridgeNetworkmapGraphviz() {

        return baseTopic.concat("/bridge/networkmap/graphviz");
    }

    /**
     * @return {@literal <baseTopic>/bridge/config/permit_join}
     */
    public String getTopicBridgePermitjoin() {

        return baseTopic.concat("/bridge/config/permit_join");
    }

    /**
     * @return {@literal <baseTopic>/bridge/config/log_level}
     */
    public String getTopicBridgeLoglevel() {

        return baseTopic.concat("/bridge/config/log_level");
    }

    /**
     * @return {@literal <baseTopic>/bridge/config}
     */
    public String getTopicBridgeConfig() {

        return baseTopic.concat("/bridge/config");
    }

    /**
     * @return {@literal <baseTopic>/bridge/log}
     */
    public String getTopicBridgeLog() {

        return baseTopic.concat("/bridge/log");
    }

    /**
     * @return {@literal <baseTopic>/bridge/config/devices}
     */
    public String getTopicBridgeConfigDevices() {

        return baseTopic.concat("/bridge/config/devices");
    }

    /**
     * @return {@literal <baseTopic>/bridge/config/devices/get}
     */
    public String getTopicBridgeConfigDevicesGet() {

        return baseTopic.concat("/bridge/config/devices/get");
    }

    /**
     * @param deviceId
     * @return {@literal <baseTopic>/<deviceId>}
     */
    public String getTopicDevice(String deviceId) {

        return baseTopic.concat("/" + deviceId);
    }

    /**
     * @param deviceId
     * @return {@literal <<baseTopic>/<deviceId>/get}
     */
    public String getTopicDeviceGet(String deviceId) {

        return baseTopic.concat("/" + deviceId + "/get");
    }

}
