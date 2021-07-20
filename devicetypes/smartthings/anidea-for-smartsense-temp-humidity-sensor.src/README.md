**Anidea for SmartSense Temp/Humidity Sensor** is a modification of the stock **SmartSense Temp/Humidity Sensor** DTH (as downloaded from the SmartThingsPublic Master branch on 28/5/21) 
to support the *Sonoff SNZB-02* sensor, which identifies as the *eWeLink TH01*. **The stock handler has now been modified in a similar fashion and should go into production in August 2021. This doesn't correct the missing units on the battery event or the temperature description.**

The addition to the name is purely to avoid accidental confusion and conflict with the stock handler. For the same reason the device fingerprints that were already in the
handler have been commented out.

The changes apart from the above are:

* The metadata to run locally has been removed, though it would probably be OK with it.
* A fingerprint has been added for the *eWeLink TH01*.
* The mobile app doesn't seem to like handlers not having their own presentation so the `generic-humidity` vid was added.
* An `isSonoff()` method was added in a similar form to the the `isFrient()` and called in the `configure()` method.
* The units were added to `battery` events and the event description for `temperature` corrected.

Observations:

* The `generic-humidity` vid is missing the `%` in the dashboard tiles.

