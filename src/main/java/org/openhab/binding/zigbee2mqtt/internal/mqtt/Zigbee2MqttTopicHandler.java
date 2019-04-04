package org.openhab.binding.zigbee2mqtt.internal.mqtt;

public class Zigbee2MqttTopicHandler {

    private String baseTopic = null;

    /**
     * Set baseTopic to default name 'zigbee2mqtt'.
     *
     */
    public Zigbee2MqttTopicHandler() {
        super();
        this.baseTopic = "zigbee2mqtt";
    }

    /**
     * @param baseTopic
     */
    public Zigbee2MqttTopicHandler(String baseTopic) {
        super();
        this.baseTopic = baseTopic;
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
     * @param ieeeAddr
     * @return {@literal <baseTopic>/<ieeeAddr>}
     */
    public String getTopicDevice(String ieeeAddr) {

        return baseTopic.concat("/" + ieeeAddr);
    }

    /**
     * @param ieeeAddr
     * @return {@literal <<baseTopic>/<ieeeAddr>/get}
     */
    public String getTopicDeviceGet(String ieeeAddr) {

        return baseTopic.concat("/" + ieeeAddr + "/get");
    }

    /**
     * @param ieeeAddr
     * @return {@literal <<baseTopic>/<ieeeAddr>/set}
     */
    public String getTopicDeviceSet(String ieeeAddr) {

        return baseTopic.concat("/" + ieeeAddr + "/set");
    }
}
