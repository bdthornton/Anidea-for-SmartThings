#### [Anidea for SmartThings](../../README.md) - &copy; Graham Johnson (orangebucket)

# Custom Capabilities (circlemusic21301)

A number of custom capabilities have been built for use with the Groovy DTHs. They use the namespace `circlemusic21301`.

Filenames have the form `capabilityId.json` for capability and `capabilityId-presentation.json` for the associated presentation.

Capability|Description
|--|--|
Contact Commands|Define setter commands for the standard Contact Sensor capability.
Motion Commands|Define setter commands for the standard Motion Sensor capability.
Occupancy Commands|Define setter commands for the standard Occupancy Sensor capability.
Presence Commands|Define setter commands for the standard Presence Sensor capability.
Water Commands|Define setter commands for the standard Water Sensor capability.

The '... Commands' custom capabilities define commands for setting the attribute of the related standard capabilities to the active or inactive state.
As they do not have attributes they do not define dashboard states or automation conditions. Support of commands for setting particular attribute values 
is lagging behind single setter commands, but when the technology permits the dashboard actions and details view will be supported. The automation actions
are already being supported.
