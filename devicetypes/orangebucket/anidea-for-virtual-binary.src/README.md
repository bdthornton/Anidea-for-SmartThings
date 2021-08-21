#### [Anidea for SmartThings](../../../README.md) > [Anidea for Virtual Devices](../README.md#anidea-for-virtual-devices) - (C) Graham Johnson (orangebucket)
---

# Anidea for Virtual Binary
This handler implements a multiple attribute binary state device comprised of a custom Binary Sensor capability, a Switch, and a number of binary state stock capabilities which can be individually enabled. The intention is for the attributes to be linked so that if one is active or inactive, all the other enabled capabilities are also active or inactive. There is, however, a device preference that can be left false to allow the attributes to all function independently. This is not supported in the app UI.

The supported capabilities, which with the exception of Switch are all disabled by default and should be enabled as required using the device settings, are:

|CAPABILITY|ATTRIBUTE|ACTIVE STATE|COMMAND|INACTIVE STATE|COMMAND|
|----------|---------|--------------|----------------|--------------|----------------|
|Binary Sensor (Custom)|binarysensor|active|binaryactive()|inactive|binaryinactive|
|Contact Sensor|contact|open|open()|closed|close()|
|Motion Sensor|motion|active|active()|inactive|inactive()|
|Occupancy Sensor|occupancy|occupied|occupied()|unoccupied|unoccupied()|
|Presence Sensor|presence|present|arrived()|not present|departed()|
|Switch|switch|on|on()|off|off()|
|Water Sensor|water|wet|wet()|dry|dry()|

The commands for the stock capabilities are consistent with those used by other 'Anidea for ...' device handlers. Those are derived from the capability where the device is an actuator, from the commands used by a stock 'Simulated ...' device handler where one is available, and lastly from whatever has been chosen for use in other 'Anidea for ...' device handlers.
*The one exception is that `wet()` and `dry()` were created for the handler.*

The Switch capability is permanently enabled as its presentation includes an on/off button and the SmartThings app returns an error if pushing that button doesn't result in an attribute change. It is therefore a sensible candidate for use on the dashboard tile for both state and action.

The other capabilities were made optional as it just seemed a better idea not to fire off events that weren't needed. The mobile app also responds particularly enthusiastically when the Water Sensor detects moisture so it seemed better to prevent that happening unexpectedly. Currently unused capabilities are still displayed in the app but this is being looked into as it should be possible to prevent it.

The custom commands have been implemented as custom capabilities. The app still doesn't seem to support a simple toggle switch widget with binary commands so they are not currently available in the app.

The icon displayed in the app is a generic SmartThings device icon as it isn't possible to dynamically change it in the handler. Should anyone want to tweak the handler, they might like to know that alternative values for `ocfDeviceType` could be:

|CAPABILITY|DEVICE TYPE|
|----------|-----------|
|Contact Sensor|x.com.st.d.sensor.contact|
|Motion Sensor|x.com.st.d.sensor.motion|
|Presence Sensor|x.com.st.d.sensor.presence|
|Switch|oic.d.switch|
|Water Sensor|x.com.st.d.sensor.moisture|

There doesn't seem to be a supported device type for an Occupancy Sensor. It should be possible to change the `ocfDeviceType` without any problems, but any changes to supported capabilities might well require a new `vid`.
