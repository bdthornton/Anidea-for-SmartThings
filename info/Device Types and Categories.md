# Device Types and Categories

When creating a device integration via the *Developer Workspace* you are required to create a *Device Profile*. During this process you choose a *Device Type* from 
a drop down list and this is ultimately used to define the icon used for the device in the mobile apps.

When creating legacy integrations using a Groovy *Device Type Handlers (DTH)*, this device type can be added to the `metadata{}` as the `ocfDeviceType` argument
to the `definition()` method. For example:

```
definition( name: 'Device Name', namespace: 'GitHub Username', author: 'Real Name',
    			  ocfDeviceType: 'x.com.st.d.doorbell', mnmn: 'SmartThingsCommunity', vid: '12345678-1234-1234-1234-f123456789012' )
```

When creating an integration for a hub connected device using an *Edge Driver* you define a *Profile* for the device in the driver. Although it seems to do broadly the same job
as the *Device Profile* mentioned earlier, it seems to differ in detail. For example, the CLI can retrieve either sort of profile from the *deviceprofiles* endpoint of the API
using its UUID, but only the new form is recognised. Conversely when generating a sample *Device Config* from a profile (`smartthings presentation:device-config:generate`),
it is only the older form that can be used.

It seems that the intention is for the device type to be indicated using a *Category*, or indeed more than one. A category is a PascalCase name such as
`DoorBell`. The mobile app is able to use the icon indicated by the category when a device is installed but then drops it. The workaround is to add an `ocfDeviceType` to the
`metadata` in the profile file, just as with the DTH.

The following list of device types was extracted from the *Developer Workspace* on **9th October 2021**. It is believed that the PascalCase form required for categories can be
created by removing the spaces from the names in the first column, though it isn't guaranteed.

Device Type|ocfDeviceType
:---|:---
Air Conditioner|oic.d.airconditioner
Air Purifier|oic.d.airpurifier
Air Quality Detector|x.com.st.d.airqualitysensor
Battery|x.com.st.d.battery
Blind|oic.d.blind
Blu-ray Player|x.com.st.d.blurayplayer
Camera|oic.d.camera
Contact Sensor|x.com.st.d.sensor.contact
Cooktop|x.com.st.d.cooktop
Dishwasher|oic.d.dishwasher
Door Bell|x.com.st.d.doorbell
Dryer|oic.d.dryer
Elevator|x.com.st.d.elevator
Fan|oic.d.fan
Feeder|x.com.st.d.feeder
Garage Door|oic.d.garagedoor
Gas Valve|x.com.st.d.gasvalve
Health Tracker|x.com.st.d.healthtracker
Hub|x.com.st.d.hub
Humidifier|x.com.st.d.humidifier
IR Remote|x.com.st.d.irblaster
Irrigation|x.com.st.d.irrigation
Leak Sensor|x.com.st.d.sensor.moisture
Light|oic.d.light
Light Sensor|x.com.st.d.sensor.light
Massage Chair|x.com.st.d.massagechair
Motion Sensor|x.com.st.d.sensor.motion
MultiFunctional Sensor|x.com.st.d.sensor.multifunction
Network Audio|oic.d.networkaudio
Others|oic.wk.d
Oven|oic.d.oven
Power Meter|x.com.st.d.energymeter
Presence Sensor|x.com.st.d.sensor.presence
Refrigerator|oic.d.refrigerator
Remote Controller|x.com.st.d.remotecontroller
Robot Cleaner|oic.d.robotcleaner
Siren|x.com.st.d.siren
Smart Lock|oic.d.smartlock
Smart Plug|oic.d.smartplug
Smart Tag|x.com.st.d.tag
Smoke Detector|x.com.st.d.sensor.smoke
Solar Panel|x.com.st.d.solarPanel
Sound Sensor|x.com.st.d.sensor.sound
Stove|x.com.st.d.stove
Switch|oic.d.switch
Television|oic.d.tv
Thermostat|oic.d.thermostat
Vent|x.com.st.d.vent
Voice Assistance|x.com.st.d.voiceassistance
Washer|oic.d.washer
Water Heater|x.com.st.d.waterheater
Water Valve|oic.d.watervalve
WiFi Router|oic.d.wirelessrouter
Wine Cellar|x.com.st.d.winecellar
