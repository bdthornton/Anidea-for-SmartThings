**Anidea for Zigbee Button** is a modification of the stock **Zigbee Button** DTH (as downloaded from the SmartThingsPublic Master branch on 28/5/21) 
to support the *Sonoff SNZB-01* button, which identifies as the *eWeLink WB01*.

The addition to the name is purely to avoid accidental confusion and conflict with the stock handler. For the same reason the device fingerprints that were already in the handler
have been commented out.

The changes apart from the above are:

* A fingerprint has been added for the *eWeLink WB01*.
* Code has been added to handle `pushed`, `pushed_2x` and `held` events to coincide with the button's single, double and long presses (the long press is much longer than a typical 'held' event).
* The DTH doesn't support the `supportedButtonValues` attribute so that has been added, but only for the *eWeLink WB01*.
* The method handling battery levels rejects voltages over 3.0 V and that seems rather odd as new batteries often exceed that by some distance. The *eWeLink WB01* appears to report 3.8 V with a new battery (high but plausible). The restriction has been removed.

Observations:

* The hold time setting is for other supported buttons that send button pressed and released events, not the *eWeLink WB01*.
* For some reason, when pairing the device using 'Scan Nearby' the button pairs silently and you are not offered the option to rename it.
* The source handler had a number of Zigbee configuration commands. It isn't known if they have any bearing on the *eWeLink WB01*.
* The *Zigbee Button* handler doesn't give the impression of having had all that much attention. Perhaps there was a better starting point?

