/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
public class PropertyDiscovery extends DiscoveryResult {

    private String key = null;
    private String value = null;

    /**
     * @param ieeeAddr
     */
    public PropertyDiscovery(String topic, String ieeeAddr) {
        super(topic, ieeeAddr);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyDiscovery [ieeeAddr=" + getIeeeAddr() + ", topic=" + getTopic() + ", key=" + key + ", value="
                + value + "]";
    }
}
