#### [Anidea for SmartThings](../../../README.md) > [Anidea for Virtual Devices](../README.md#anidea-for-virtual-devices) - (C) Graham Johnson (orangebucket)
---

# Anidea for Virtual Presence
The Simulated Presence Sensor doesn't allow for the Occupancy Sensor capability that was used in mobile presence for a number of years. This handler supports both the Presence Sensor and Occupancy Sensor capabilities independently, and supports the `arrived()` and `departed()` custom commands to set presence, and uses `occupied()` and `unoccupied()` for occupancy.

A breaking change was made to mobile presence on Android in April/May 2021 without any comment. The Occupancy Sensor capability was completely removed. To help mitigate the effects of this, an option to link changes to the Presence Sensor and Occupancy Sensor was added to the handler settings. As was the case with the mobile presence,  `present` events are sent before `occupied`, and `unoccupied` events are sent before `not present`.

The [Anidea for Virtual Binary](../anidea-for-virtual-binary.src/) handler is an alternative if you just want presence, or you want to link presence with other binary attributes.
