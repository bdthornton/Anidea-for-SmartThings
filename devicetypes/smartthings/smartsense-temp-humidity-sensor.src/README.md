**Anidea for SmartSense Temp/Humidity Sensor** was modification of the stock **SmartSense Temp/Humidity Sensor** DTH (as downloaded from the SmartThingsPublic Master
branch on 28/5/21) to support the *Sonoff SNZB-02* sensor, which identifies as the *eWeLink TH01*. The addition to the name was purely to avoid accidental confusion and conflict with the stock handler. For the same reason the device fingerprints that were already in the handler were commented out.

The changes apart from the above were:

* The metadata to run locally was removed, though it would probably be OK with it.
* A fingerprintwas for the *eWeLink TH01*.
* The mobile app didn't seem to like handlers not having their own presentation so the `generic-humidity` vid was added.
* An `isSonoff()` method was added in a similar form to the the `isFrient()` and called in the `configure()` method.
* The units were added to `battery` events and the event description for `temperature` corrected.

Observations:

* The `generic-humidity` vid is missing the `%` in the dashboard tiles.

The stock handler was modified for the *eweLink TH01* in mid-July 2021 and went into production on the 27th. Consequently the **Anidea for SmartSense Temp/Humidity Sensor** was
withdrawn from the repository. Ignoring the largely cosmetic logging mistakes, there is a glaring problem in that the handler sets the temperature reporting interval for the
device to a minimum of one hour and a maximum of two hours, yet the checkInterval remains at twelve minutes for the default thirty second to five minute range of the other
devices. That means the device is frequently being reported as offline.

Tests are underway with a minimally modified custom handler that just changes the `configure()` method to set the temperature reporting to something sensible and leaves
everything else as is in the hope of retaining correct local execution. This would have to be manually added.
