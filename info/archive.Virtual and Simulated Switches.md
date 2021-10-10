# SmartThings: Virtual and Simulated Switches

SmartThings has stock device type handlers for a *Virtual Switch* and a *Simulated Switch*. They are interchangeable for many purposes but there
are differences between the two.

## Virtual Switch
The *Virtual Switch* DTH was presumably introduced, along with the *Virtual Dimmer Switch*, to work with the *Virtual Device Creator* SmartApp.
It seems likely to be part of an attempt to bring virtual devices into the production environment and make them easily accessible to all users, 
rather than requiring the IDE to be used. Unfortunately SmartThings seem to have pretty much lost interest in developing the concept. The key points are:

* The device handler supports the `Switch`, `Actuator` and `Sensor` capabilities.
* The device handler can run locally.
* Device Health is NOT supported.
* Only the standard `on` and `off` commands are available.
* The ‘on’ and ‘off’ events are sent with ‘isStateChange: true’ so the events are always propagated even if the state hasn’t changed.
* Historical: The Classic UI had on and off buttons to force a particular state, as well as the main button for toggling.

The `isStateChange: true` setting allows the switch to operate rather like two stateless buttons, only with the values `on` and `off` rather than `pushed`. It is important to be
aware of this behaviour when using pairs of automations of the `if A is set then turn switch on` and `if switch is on then set A` as it places the burden of preventing an infinite
loop elsewhere.

## Simulated Switch
The *Simulated Switch* is one of a number of simulated devices grouped together in a `testing` sub-folder of the SmartThins public GitHub repository. These devices were 
clearly created for testing purposes, but naturally many users have found a use for them in their production environments. The key points are:

* The device handler supports the `Switch`, `Actuator`, `Sensor`, `Relay Switch` (obsolete) and `Health Check` capabilities.
* The device handler can NOT run locally.
* Device Health is supported.
* As well as the standard `on` and `off` commands, there are custom `onPhysical` and `offPhysical` commands which mark events as physical interactions, and  `markDeviceOnline` 
and `markDeviceOffline` commands to change the health status.
* Events do not explicitly set the ‘isStateChange’ flag and so apps only ever see alternating values of `on` and `off`.
* Historical: The Classic UI has buttons to send the four custom commands, as well as toggling.

It could be argued that a `virtual switch` is a thing in its own right and that is what the *Virtual Switch* device handler implements. The *Simulated Switch*
would also do the job. However if you want to go beyond simple `on`/`off` status and control and simulate things like the switch being offline, or physical v digital activation (although this is a largely deprecated thing), then you need the *Simulated Switch*.
