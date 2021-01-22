#### [Anidea for SmartThings](../../../README.md) > [Anidea for Odds and Sods](../README.md#anidea-for-odds-and-sods) - (C) Graham Johnson (orangebucket)
---

# Anidea for Security System Helper
This is an experimental handler that attempts to support the SmartThings Home Monitor in a more elegant fashion than using virtual switches. _On the face of things, the
`Security System` capability should be tailor made for the job, but it simply doesn't seem to work as a general purpose capability in Automations._

The handler implements a composite device
that creates three child component devices using the Anidea for Virtual Button device handler which can be used for activating the Disarmed, Armed (Stay) and Armed (Away) 
states via an Automation in the ST mobile app. The main component implements a custom SHTM Status capability that can be set to any of the three security system states
in order to expose them outside Automations and the Rules API, and also the Notification capability to provide a command to set this capability in Automations, something
which the custom capability can not yet do at the time of writing. *The deviceNotification attribute of the Notification capability does not seem to be working correctly as 
an Automation action but does work in the Rules API.*

What hasn't been nailed so far is a way of elegantly displaying the STHM Status in something like ActionTiles without too much mucking around.
