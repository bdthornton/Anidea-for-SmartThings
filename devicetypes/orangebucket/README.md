#### [Anidea for SmartThings](../../README.md) - (C) Graham Johnson (orangebucket)
---

# Anidea for Groovy Device Handlers

- [Anidea for Lumi Devices](#anidea-for-lumi-devices)
- [Anidea for Virtual Devices](#anidea-for-virtual-devices)
- [Anidea for Odds and Sods](#anidea-for-odds-and-sods)

---

## Anidea for Lumi Devices
<img src="../../images/aqara_button.png?raw=true" width="100"><img src="../../images/aqara_contact.png?raw=true" width="100"><img src="../../images/aqara_motion.png?raw=true" width="100"><img src="../../images/aqara_temperature.png?raw=true" width="100"><img src="../../images/aqara_vibration.png?raw=true" width="100">

**I have now stopped using Aqara sensors. They are said to work very well with their native gateways but those gateways do not have an official integration with Smartthings. I eventually decided that the direct hub integration with the SmartThings hubs was just more trouble that it is was worth. It was nothing to do with stability (the devices are very stable, or the functionality (which is very good), but simply because they can be ridiculously dificult to get to pair and they are very fussy about the routers they work with which severely limits what else can be on the mesh with them. They also don't seem terribly keen on the dynamic nature of the mesh.**

**SmartThings are replacing Groovy Device Type Handlers with Edge Drivers and they entered beta in August 2021. I have no intention of reimplementing these handlers in Edge Drivers due to a lack of knowledge, resources and indeed enthusiasm. Fortunately it does appear that other community developers who have persisted with the devices have taken on the task of creating integrations for Aqara devices going forward.**

- [Anidea for Aqara Button](anidea-for-aqara-button.src/)
- [Anidea for Aqara Contact](anidea-for-aqara-contact.src/)
- [Anidea for Aqara Motion](anidea-for-aqara-motion.src/)
- [Anidea for Aqara Temperature](anidea-for-aqara-temperature.src/)
- [Anidea for Aqara Vibration](anidea-for-aqara-vibration.src/)
- [Anidea for Mijia Contact](anidea-for-aqara-contact.src/)
  
The ['bspranger' device handlers](https://github.com/bspranger/Xiaomi) are the results of a cumulative community effort to support the Mijia and Aqara brands of sensors made by Lumi, but generally referred to by the Xiaomi name. The work seems to have been largely driven by different single individuals at different times, with particular mentions due to Wayne Man ('a4refillpad') for creating the first set of handlers, Brian Spranger ('ArstenA' / 'bspranger') for taking the work forward, and Keith Gaumont ('veeceeoh') for keeping it going. The original handlers also credit Alec McLure ('alecm'), Alix JG ('alixjg'), Christian Scheiene ('cscheiene'), 'gn0st1c', 'foz333', Jon Magnuson ('jmagnuson'), 'rinkek', Ron van de Graaf ('ronvandegraaf'), 'snalee', Steven Dale ('tmleafs'), Andy ('twonk') and Christian Paiva ('xtianpaiva'), with apologies to anyone that has been overlooked.

The sensors use Zigbee in a rather non-standard way and so they need special handling. SmartThings recognise their popularity enough to make some allowances for them, but neither they nor Lumi have created 'official' handlers. Although they are very effective, the handlers are not without issues, and in particular they are very much rooted in the Classic environment and some of the the Health Check support isn't quite right. There are other things about them which are more about personal taste: the logging might be considered a bit excessive and untidy; there are several custom attributes that don't really add anything; and most of the settings are pretty much superfluous (for example, why have a UK / US date format setting when log messages are already timestamped?). 

The 'Anidea for ...' handlers strip things down and make them suitable for the 'new' app and environment, with the Classic app no longer supported. Suitable custom capabilities and device presentations will be created where required, but the tools required only entered alpha test in mid-June 2020 and the implementation still isn't quite up to scratch in October 2021.

**Although a lot of edits have been made to the device handlers, they remain underpinned by the code from the 'bspranger' handlers when it comes to the Zigbee side of things, and also when it comes to the maths used in the Vibration Sensor.**

The common changes made to all the handlers include:

* Completely remove the `tiles()` section as the Classic app is not being supported.
* Remove custom attributes and commands, except for custom 'setter' commands (used to force attributes to particular values).
* Rename custom setter commands where they differ from the equivalent commands in ST stock handlers.
* Define the custom setter commands using custom capabilities rather than using `command`.
* Initialise all attributes in the `installed()` method (the 'new' app isn't keen on attributes without values).
* Initialise `checkInterval` to twenty-four hours as battery reports take a few hours to appear.
* Set a `checkInterval` of two hours ten minutes once the first of the regular battery reports has arrived.
* Fix 0% and 100% battery levels to 2.7 V and 3.2 V rather than having settings.
* Change logging to 'house style', using `info` for each method entered, and `debug` for finer details.
* Change code to 'house style': lower case variable and method names except where required for compatibility; Allman style indentation; single quotes where possible; spaces around contents of brackets and parentheses.

---
## Anidea for Virtual Devices

**I have no real intention of reimplementing these DTHs as Edge Drivers. I am already using community written Edge Drivers for most of my own requirements, and am also aware that Edge Drivers can never be the complete answer going forwards as they don't allow for cloud execution. SmartThings are very aware that much of the use of virtual devices doesn't really require the device paradigm at all and seem intent on providing more appropriate solutions, as well as their own virtual devices. So I am happy to bide my time.**

- [Anidea for Virtual Binary](anidea-for-virtual-binary.src)
- [Anidea for Virtual Button](anidea-for-virtual-button.src)
- [Anidea for Virtual Humidity](anidea-for-virtual-humidity.src)
- [Anidea for Virtual Momentary](anidea-for-virtual-momentary.src)
- Anidea for Virtual Presence (REMOVED)
- [Anidea for Virtual Temperature](anidea-for-virtual-temperature.src)
  
At the time I built the [Anidea for Virtual Button](anidea-for-virtual-button.src) handler, there simply wasn't a stock handler that implemented a virtual button with the momentary capability and worked cleanly with the 'new' SmartThings mobile app. Once I put that together, I considered adding the Switch capability, as used by the stock Momentary Button Tile handler, and also Contact Sensor and Motion Sensor capabilities as I was aware that could be useful for triggering Alexa routines. As adding those capabilities made the device details page look a bit of a mess, and more significantly made the `contact` attribute the default tile status instead of `button` (which could not be corrected at the time), I decided to create a separate handler instead, hence [Anidea for Virtual Momentary](anidea-for-virtual-momentary.src). I also felt an optional delay between the momentary active and inactive actions would be useful. Having a Momentary action without a Button seemed wrong so I added that, so the handler really became an extension of [Anidea for Virtual Button](anidea-for-virtual-button.src).

It was obviously useful to be able to do things like map `switch` attributes to `contact` attributes, and vice versa. Hence the [Anidea for Virtual Binary](anidea-for-virtual-binary.src) handler. This can be useful for Alexa routines and it is particularly useful as an alternative to stock simulated sensors when the setter commands aren't available. 

Mobile presence had been using both the Presence Sensor and Occupancy Sensor capabilities for some time. The Anidea for Virtual Presence handler did likewise. When mobile presence stopped using the Occupancy Sensor I added an option to link then both, and eventually made the linking optional in the [Anidea for Virtual Binary](anidea-for-virtual-binary.src) handler so I could remove Anidea for Virtual Presence completely.

A post on Facebook mentioned that the Simulated Temperature Sensor didn't work with the new app. This led to [Anidea for Virtual Temperature](anidea-for-virtual-temperature.src) being created. A few months later a similar appeal was made for devices handlers for virtual humidity so that led to [Anidea for Virtual Humidity](anidea-for-virtual-humidity.src).

---

## Anidea for Odds and Sods

- [Anidea for HTTP Ping](anidea-for-http-ping.src)
- Anidea for Scene Momentary (REMOVED)
 
The last group of DTHs probably aren't of any practical use to other users though there might be something in the code that is of interest. [Anidea for HTTP Ping](anidea-for-http-ping.src) was written to detect a TV being switched on as active motion, back when I thought Smart Lighting behaved sensibly with multiple motion sensors (it probably didn't, I probably didn't test it well enough). Anidea for Scene Momentary was a momentary button that activated a scene, which could have been useful in ActionTiles pending the arrival of the official integration (which is now live), though it was also easy to use a bit of JavaScript in a URL Shortcut or a virtual switch with a trivial automation. It has been removed as it was a bit too pointless.

- [Anidea for Security System Helper](anidea-for-security-system-helper.src)

The [Anidea for Security System Helper](anidea-for-security-system-helper.src) DTH is also unlikely to be particularly useful to other users. I developed it to assist with controlling STHM, not that I particularly wanted to do such a thing apart from perhaps in ActionTiles. The standard trick of using three virtual switches and six Automations apparently never worked for everyone for some reason which wasn't clear and certainly didn't work properly for me (though I was probably making things too complicated by using webCoRE and trying to retain the old alarm system access, which was still working even without SHM). Even before the switch to Rules based Automations meant the Virtual Switch DTH was no longer suitable, I thought the approach was just wrong.
