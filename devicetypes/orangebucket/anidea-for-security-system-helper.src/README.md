#### [Anidea for SmartThings](../../../README.md) > [Anidea for Odds and Sods](../README.md#anidea-for-odds-and-sods) - (C) Graham Johnson (orangebucket)
---

# Anidea for Security System Helper
The legacy _Smart Home Monitor (SHM)_ status was actually the `alarmSystemStatus` attribute of the `Alarm System` capability in the SmartThings `location` and access to read and write this attribute was freely available to apps. The _SmartThings Home Monitor (STHM)_ works with the more conventionally defined `Security System` capability, again associated with the `location` and often referred to as the _Security Mode_. Access to the _Security Mode_ is more carefully controlled, currently being restricted to _Automations_ and the _Rules API_.

There is demand for access to the _Security Mode_ in third party legacy apps such as _webCoRE_ or _ActionTiles_, and the typical way of achieving this is to use three virtual switches to represent the three modes. Six _Automations_ are used, three to set the switches appropriately when the _Security Mode_ changes, and three to set the _Security Mode_ when a particular switch is turned on. There are some issues with this:
* For some reason it doesn't work for everyone. The switches somehow get out of sync.
* Having multiple switches changing at the same time isn't ideal for _webCoRE_ as it can throw up semaphore waits to confuse things.
* Setting a switch from the mode, and setting the mode from the switch, relies on there being mechanisms to break the obvious infinite loop. This is particular a danger
with the _Virtual Switch_ DTH which flags all events as state changes so they get propagated, instead of the default of only propagating actual changes.

This, still experimental, handler attempts to address the problem differently by separating out activation and status. The following factors have influenced the design.

* Although creating a handler with the `Security System` capability would seem to be almost 'job done', the capability simply wasn't implemented in _Automations_ at all. Things might perhaps have moved on but actually there is a lot to be said for using more established stock capabilities anyway.
* The Momentary capability, combined with the Button capability, seems more appropriate than using the Switch capability. It is certainly more _webCoRE_ friendly.
* _Custom Capabilities_ were yet to be implemented in _Automations_ so a suitable stock capability was needed to allow a command to be sent. The _Notification_ capability seemed to be an appropriate choice. As it turned out *Automations* returned an error when trying to configure the `deviceNotification` command (this may still be the case) but the `Rules API` could be used instead (though it took a few months after it was announced before the _Security Mode_ worked properly with the _Rules API_).
* Using a multiple component device seemed more appropriate than separate child devices, which again caused delays as reality caught up with theory.
* Although the handler is not particularly intended to be _STHM_ specific, attribute names exist in a single namespace in legacy handlers so it was less confusing to have _STHM_ in the custom capability name.

The handler is comprised of a main component which has the `Notification` and `STHM Status (circlemusic21301.sthmStatus)` capabilities, and three components, `away`, `stay` and `disarm`, using the [Anidea for Virtual Button](../anidea_for_virtual_button.src) handler. Legacy apps will see the child devices with names appended with `Arm (Away)`, `Arm (Stay)` and `Disarm.

Setting the _Security Mode_ requires an _Automation_ to be created that acts on the appropriate component `button` being `pushed`. At the time of writing the `Automation` creator requires you to select the appropriate `Pressed` (sic) value from a list, but unfortunately it doesn't filter out the unused values (making the list really long) and nor does it separate the list up into the different components (let alone capabilities or attributes) so you have to work out which `Pressed` you want.
