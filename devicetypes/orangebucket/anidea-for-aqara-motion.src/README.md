#### [Anidea for SmartThings](../../../README.md) > [Anidea for Lumi Devices](../README.md#anidea-for-lumi-devices) - (C) Graham Johnson (orangebucket)
---

# Anidea for Aqara Motion
This supports the same Aqara motion sensors as the original 'bspranger' handler, providing both Motion and Illuminance. The sensors do not send inactive reports so the device handler resets motion using a timer. I used a number of sensors with a sixty second timer for a considerable time without any obvious issue but, as this matches the 'blind' period of the sensors, and SmartThings times aren't particularly precise, it seems better to relax the default period to 65 seconds to avoid any race condition.

During April 2020, I experienced a considerable number of issues with the motion not being reset, and all the indications were that the timer event simply wasn't being received. For this reason, `active()` and `inactive()` custom commands have been added, matching the names used in the Simulated Motion Sensor, to allow the attribute to be 'reset' to a known state. The `active()` command does not set the timer.

At the end of August 2020, I noticed that every single one of my Aqara motion sensors was showing 'Checking status' in the app. The fix seemed to be to add an explicit 'vid' to the DTH.

**I NO LONGER ACTIVELY USING THESE SENSORS.**
