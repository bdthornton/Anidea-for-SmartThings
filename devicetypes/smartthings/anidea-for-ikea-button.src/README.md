**Anidea for Ikea Button** is a modification of the stock **Ikea Button** DTH (as downloaded from the SmartThingsPublic Master branch on 27/5/21) 
to support the *IKEA Tradfri Shortcut Button*.

The addition to the name is purely to avoid accidental confusion and conflict with the stock handler. For the same reason the device fingerprints that were already in the handler
have been commented out.

The changes apart from the above are:

* A fingerprint has been added for the *Shortcut Button* which also includes a custom presentation in the `SmartThingsCommunity` namespace that is derived from that for the _On/Off Switch_.
* Various methods have added to support the *Shortcut Button* in the same way as those used for other devices to define the buttons and labels and for use as booleans.
* The *Shortcut Button* has been added to `isIkea()` which is used to prevent the battery level being divided by 2 (the DTH also supports another brand).
* A component is used even for a single button as there is only an event history for the components (a condition was commented out).
* Code to handle the *Shortcut Button* was added to `getButtonEvent()` treating it as a single button version of the _On/Off Switch_.

Observations:

* The original DTH wasn't written to allow for the `installed()` method to run more than once so it will try to create child devices again and return an error.
* The onboarding process works smoothly if you pair the button as an Ikea 'Button'. If you use 'Scan Nearby' it tends to pair silently and you don't get the chance to rename the button (but not always). No idea why that is.
* The dashboard tile has a tendency to show either 'Checking ...' or nothing at all in the status, and the details page does not overwrite the default label for the button. This is despite the API showing the device has the correct presentation and the presentation showing the correct label. Also the details page does seem to respect the presentation in other respects.
