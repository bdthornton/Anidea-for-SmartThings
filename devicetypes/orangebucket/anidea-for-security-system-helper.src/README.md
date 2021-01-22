#### [Anidea for SmartThings](../../../README.md) > [Anidea for Odds and Sods](../README.md#anidea-for-odds-and-sods) - (C) Graham Johnson (orangebucket)
---

# Anidea for Security System Helper
The legacy _Smart Home Monitor (SHM)_ status was actually the `alarmSystemStatus` attribute of the `Alarm System` capability in the SmartThings `location` and access to read and write this attribute was freely available to apps. The _SmartThings Home Monitor (STHM)_ works with the more conventionally defined `Security System` capability, again associated with the `location` and often referred to as the _Security Mode_. Access to the _Security Mode_ is more carefully controlled, currently being restricted to _Automations_ and the _Rules API_.

There is demand for access to the _Security Mode_ in third party legacy apps such as _webCoRE_ or _ActionTiles_, and the typical way of achieving this is to use three virtual switches to represent the three modes. Six _Automations_ are used, three to set the switches appropriately when the _Security Mode_ changes, and three to set the _Security Mode_ when a particular switch is turned on. There are some issues with this:
* For some reason it doesn't work for everyone. The switches somehow get out of sync.
* Having multiple switches changing at the same time isn't ideal for _webCoRE_ as it can throw up semaphore waits to confuse things.
* Setting a switch from the mode, and setting the mode from the switch, relies on there being mechanisms to break the obvious infinite loop. This is particular a danger
with the _Virtual Switch_ DTH which flags all events as state changes so they get propagated, instead of the default of only propagating actual changes.

This handler attempts to address the problem differently by separating out activation and status. The following factors have influenced the design.

* Although creating a handler with the `Security System` capability would seem to be almost 'job done', the capability simply wasn't implemented in _Automations_ at all. Things might perhaps have moved on but actually there is a lot to be said for using more established stock capabilities anyway.
* The Momentary capability, combined with the Button capability, seems more appropriate than using the Switch capability. It is certainly more _webCoRE_ friendly.
* _Custom Capabilities_ were yet to be implemented in _Automations_ so a suitable stock capability was needed to allow a command to be sent. The _Notification_ capability seemed to be an appropriate choice. As it turned out *Automations* returned an error when trying to configure the `deviceNotification` command but the `Rules API` could be used instead (though it took a few months after it was announced before the _Security Mode_ worked properly with the _Rules API_). _The deviceNotification command, which is presented as `Text Display`, is working as of January 2021._
* Using a multiple component device seemed more appropriate than separate child devices, which again caused delays as reality caught up with theory.
* Although the handler is not particularly intended to be _STHM_ specific, attribute names exist in a single namespace in legacy handlers so it was less confusing to have _STHM_ in the custom capability name.

The handler is comprised of a main component which has the `Notification` and `STHM Status (circlemusic21301.sthmStatus)` capabilities, and three components, `away`, `stay` and `disarm`, using the [Anidea for Virtual Button](../anidea_for_virtual_button.src) handler.The [device config](live_afssh_cfg.json) just specifies the Momentary button for each component in the details view, and uses `sthmStatus` for the dashboard tile status with the `disarm` momentary action for the button. 

In order to set the _Security Mode_, there is still a need to create three _Automations_, one for each mode. The condition of each _Automation_ will use the helper device. At the time of writing, the mobile app offers up a long list of possible attribute values with no sub-division into _Component_, _Capability_ or _Attribute_ as it does elsewhere. Not only that, but it isn't limited to the 'supported button values', but every single value the `Button` capability understands. So you have to work out which of the three `Pressed` (sic) values in the long list corresponds to which button.

Setting the device `sthmStatus` attribute from the _Security Mode_ can again be performed with three _Automations_, one for each mode. The action uses the helper device and the `deviceNotification` command appears as `Text Display`. The value of the `Text Display` should be set to `armedAway`, `armedStay`, or `disarmed`, as appropriate. _This was broken for a number of months but is working as of January 2021._

An alternative to using _Automations_ is to use the _Rules API_. This currently only works for reading the _Security Mode_, not for setting it. [A suitable rule has been uploaded to the repository](security_actions_redacted_rule.json) (redacted to remove the device IDs).

Legacy apps such as _webCoRE_ will see four devices for the helper. One will just show the device name/label and will include the `sthmStatus` attribute, and the other three with `Arm (Away)`, `Arm (Stay)` and `Disarm` appended to their labels will be the buttons and include the `push` command. _The `button` attributes and the `deviceNotification` command are accessible but are only meant to be used as above._

The `Momentary` buttons work nicely in _ActionTiles_ but currently displaying the status in a useful way is the gotcha. Displaying it using polling isn't such a big deal, but displaying changes instantly takes more thinking about it. The irony is that this is the one thing the author actually needs the handler for.
