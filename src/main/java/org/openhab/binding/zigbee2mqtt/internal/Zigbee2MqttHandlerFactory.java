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
package org.openhab.binding.zigbee2mqtt.internal;

import static org.openhab.binding.zigbee2mqtt.internal.Zigbee2MqttBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.zigbee2mqtt.internal.discovery.Zigbee2MqttDiscoveryService;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Zigbee2MqttHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author zigbee2mqtt - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.zigbee2mqtt", service = ThingHandlerFactory.class)
public class Zigbee2MqttHandlerFactory extends BaseThingHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(Zigbee2MqttHandlerFactory.class);

    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_GATEWAY.equals(thingTypeUID)) {

            Zigbee2MqttBridgeHandler bridgeHandler = new Zigbee2MqttBridgeHandler((Bridge) thing);
            Zigbee2MqttDiscoveryService discoveryService = new Zigbee2MqttDiscoveryService(bridgeHandler);
            bridgeHandler.setDiscovery(discoveryService);
            this.discoveryServiceRegs.put(thing.getUID(), bundleContext.registerService(
                    DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>()));

            return bridgeHandler;

        } else if (SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            return new Zigbee2MqttDeviceHandler(thing);
        }

        logger.debug("thingTypeUID not supported: {}", thingTypeUID.getAsString());

        return null;
    }

    @Override
    protected void removeHandler(ThingHandler handler) {

        if (handler.getThing().getThingTypeUID().equals(THING_TYPE_GATEWAY)) {

            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.get(handler.getThing().getUID());

            serviceReg.unregister();
            discoveryServiceRegs.remove(handler.getThing().getUID());

        }
        super.removeHandler(handler);
    }

}
