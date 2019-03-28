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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

/**
 * The {@link ChannelDiscovery} class...TODO
 *
 * @author Nils
 *
 */
public class ChannelDiscovery extends DiscoveryResult {

    private String objectId = null;
    private String type = null;
    @NonNull
    private Map<String, Object> config = new HashMap<>();

    /**
     * @param ieeeAddr
     */
    public ChannelDiscovery(String topic, String ieeeAddr) {
        super(topic, ieeeAddr);
    }

    public String getObjetcId() {
        return objectId;
    }

    public void setObjectId(String objetcId) {
        this.objectId = objetcId;
    }

    public String getType() {
        return type;
    }

    public void getType(String type) {
        this.type = type;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "ChannelDiscovery [ieeeAddr=" + getIeeeAddr() + ", topic=" + getTopic() + ", objectId=" + objectId
                + ", type=" + type + "]";
    }

}
