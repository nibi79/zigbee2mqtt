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
package org.openhab.binding.zigbee2mqtt.internal.discovery.result;

/**
 * @author Nils
 *
 */
abstract public class DiscoveryResult {

    private String topic = null;
    private String ieeeAddr = null;

    /**
     * @param ieeeAddr
     */
    public DiscoveryResult(String topic, String ieeeAddr) {
        super();
        this.topic = topic;
        this.ieeeAddr = ieeeAddr;
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

    public void setIeeeAddr(String ieeeAddr) {
        this.ieeeAddr = ieeeAddr;
    }

}
