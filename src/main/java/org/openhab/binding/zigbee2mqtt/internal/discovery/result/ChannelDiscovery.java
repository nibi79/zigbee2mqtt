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

    private String id = null;
    private String category = null;
    @NonNull
    private Map<String, Object> config = new HashMap<>();

    /**
     * @param ieeeAddr
     */
    public ChannelDiscovery(String topic, String ieeeAddr) {
        super(topic, ieeeAddr);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "ChannelDiscovery [ieeeAddr=" + getIeeeAddr() + ", topic=" + getTopic() + ", id=" + id + ", category="
                + category + "]";
    }

}
