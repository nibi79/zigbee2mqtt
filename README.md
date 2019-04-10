# Zigbee2Mqtt Binding

This binding connects openHAB with your Zigbee2Mqtt server using MQTT.

# Table of contents

1. [Disclaimer](https://github.com/nibi79/zigbee2mqtt/tree/master#disclaimer)
2. [Prerequisites](https://github.com/nibi79/zigbee2mqtt/tree/master#installation-and-upgrade)
3. [Installation and upgrade](https://github.com/nibi79/zigbee2mqtt/tree/master#installation-and-upgrade)
3. [Supported Things](https://github.com/nibi79/zigbee2mqtt/tree/master#supported-things)
4. [Discovery](https://github.com/nibi79/zigbee2mqtt/tree/master#discovery)
5. [Configuration](https://github.com/nibi79/zigbee2mqtt/tree/master#configuration)
6. [Channels](https://github.com/nibi79/zigbee2mqtt/tree/master#channels)
7. [Support](https://github.com/nibi79/zigbee2mqtt/tree/master#support)

***

## Disclaimer

This binding is currently under development. Your help and testing would be greatly appreciated but there is no stability or functionality warranty.

## Prerequisites

- A working Zigbee2Mqtt installation (https://github.com/Koenkk/zigbee2mqtt)
- Enable 'homeassistant' in data/configuration.yaml (http://www.zigbee2mqtt.io/configuration/configuration.html)

## Installation and upgrade

For an installation the [latest release](https://github.com/nibi79/zigbee2mqtt/releases) should be copied into the /addons folder of your openHAB installation.
For an upgrade the existing file should be overwritten. On major or structural changes existing things might have to be deleted and recreated, existing channels might be kept. For further information please read release notes of a corresponding release.

## Supported Things

Currently following Things are supported:

- **Zigbee2Mqtt Server** Thing representing the Zigbee2Mqtt server
- One or many Things for supported **Zigbee2Mqtt Device**

## Discovery

After configuring the **Zigbee2Mqtt Server**, automatic discovery for **Zigbee2Mqtt Device** will start. If successful, your **Zigbee2Mqtt Device** will be found and can be added without further configuration. 

If new devices joined to the network ([documentation](http://www.zigbee2mqtt.io/getting_started/pairing_devices.html)) they will automatically appear in the INBOX.

## Binding Configuration

Following options can be set for the **Zigbee2Mqtt Server**:

| Property   | Description |
|-----------|-----------|
| mqttbrokerIpAddress | IP-Address  or hostname of the MQTT-Broker|
| mqttbrokerPort | Port of the MQTT-Broker |
| mqttbrokerClientID | Client ID for MQTT-Broker. |
| mqttbrokerUsername | Username to access the MQTT-Broker |
| mqttbrokerPassword | Password to access the MQTT-Broker. |
| mqttbrokerBaseTopic | Base topic which is used by Zigbee2Mqtt. |

## Channels

### Currently following **Channels** are supported on the **Zigbee2Mqtt Server**:

| Channel   | Type | Values |
|-----------|-----------|-----------|
| permitJoin| `Switch` | ON/OFF |
| logLevel  | `String` | DEBUG, INFO, WARN, ERROR |
| networkMap| `Image` |  |

Example NetworkMap: 

![alt text](images/networkmap.png)



### Currently following **Channels** are supported on the **Zigbee2Mqtt Device**:

| Channel   | Type | Values |
|------------|-----------|-----------|
| state      | `Switch` | ON/OFF |
| temperature| `Number` | |
| illuminance| `Number` | |
| occupancy  | `Switch` | ON/OFF |
| humidity   | `Number` | |
| pressure   | `Number` | |
| contact    | `Contact` | OPEN/CLOSE |
| water_leak | `Switch` | ON/OFF |
| battery    | `Number` | |
| linkquality| `Number` | |
| brightness | `Number` | |
| color_temp | `Number` | |
| color      | `Color`  | |
| click      | `String` | This is a trigger channel and cannot be link to an item. The values depends on device e.g.: single, double, triple, quadruple ... |
| action     | `String` | This is a trigger channel and cannot be link to an item. The values depends on device e.g.: shake, wakeup, fall, tap, slide, flip180 ... |

## File based configuration
### .rules
```
 rule "Zigbee2Mqtt Cube SLIDE"
 when
   Channel "zigbee2mqtt:zigbee2mqttDevice:86a2833a:0x00158d00d38:action" triggered "slide"
 then
   logInfo("cube", "slided" )

 end
```
or
```
rule "Zigbee2Mqtt Cube action"
when
    Channel "zigbee2mqtt:zigbee2mqttDevice:86a2833a:0x00158d00d38:action" triggered
then
    switch(receivedEvent.getEvent()) {
        case "slide": {
             logInfo("cube", "cube slided" )
        }
       case "rotate_right": {
             logInfo("cube", "cube rotate_right" )
        }        
       case "rotate_left": {
             logInfo("cube", "cube rotate_left" )
        }          
       case "shake": {
             logInfo("cube", "cube shake" )
        }
       case "fall": {
             logInfo("cube", "cube fall" )
        }          
    }
end
```

## Support

If you encounter critical issues with this binding, please consider to:

- create an [issue](https://github.com/nibi79/zigbee2mqtt/issues) on GitHub
- search [community forum](https://community.openhab.org/) for answers already given
- or make a new post there, if nothing was found

In any case please provide some information about your problem:

- openHAB and binding version 
- error description and steps to retrace if applicable
- any related `[WARN]`/`[ERROR]` from openhab.log
- whether it's the binding, bridge, dvice or channel related issue

For the sake of documentation please use English language. 

