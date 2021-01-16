# SmartThings: Groovy Device Handlers (DTH)
Various notes about device handlers, including this note that the terminology has changed over time but the initialism DTH (from Device Type Handlers) seems to have stuck.

## Metadata
[Some best guess notes on how metadata used to create the UI for the SmartThings app are maintained in a separate document.](Device%20UI%20Metadata.md)

## Methods
There are a number of methods that are called automatically by the ‘system’ for what might generally be described as configuration and administration purposes,
and this includes some that are defined as commands in *Capabilities*.

|||
|:-|:-|
|`installed()`|Runs when a device is first installed, and whenever the device is updated using the IDE (and probably by the mobile apps e.g. renaming).|
|`configure()`|This is a command defined for the Configuration capability. If that capability is in use, the configure() method is called after `installed()` and can also be called manually by apps supporting the capability. The purpose of the method is to send configuration instructions to the device. Experience shows that it isn’t a good place to install and delete child devices (specifically deleting child devices in `configure()` caused subsequent calls to `getChildDevices()` in parse() to fail).|
|`updated()`|This runs when a device is first installed, when it is updated in the IDE (as long as something has actually changed), and when settings are updated in the mobile apps. The latter makes it a convenient place for semi-automatically managing child devices.|
|`poll()`|This is a command associated with the deprecated `Poll` capability. It may still get called automatically but it is preferred that device handlers request updates using `refresh()`, and schedule such things themselves.|
|`refresh()`|This is a command associated with the Refresh capability. The `refresh()` method is intended for updating the device handler status, typically byrequesting the device sends an update.|
|`parse()`|The `parse()` method (or methods, as it might be overloaded for different return values or parameters) is used to process messages sent from the device.If `parse()` returns a `Map` created by `createEvent()`) (or a `List` of `Maps`), the events will be created and propagated, and this is the typical way attributes are updated.It can do a similar thing with `HubActions`.|
|`ping()`|The largely undocumented `Health Check` capability (aka `Device Health`) uses the `ping()` command when a trackeddevice doesn't have any actively within a defined `checkInterval` in order to give it one last nudge before marking it offline. The contents could pretty much bethe same as `refresh()`.|

## Command Methods

Devices are made to ‘do’ things by calling command methods in the device handler, for example on() or off(). These are defined by the Capabilities.

When called as a command method, any returned Z-Wave or Zigbee commands, or `HubActions`, will automatically be processed. This will not apply if the method is not
called as a command. For example, `refresh()` is not being used as a command if the device handler calls if from the scheduler.
