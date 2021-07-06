The device type handlers in this folder are modification of stock handlers. The basic approach is:

* Remove any local execution metadata as that will only confuse the issue.
* Add 'Anidea for ' to the names so it is clear which DTH is being used.
* Add fingerprints as required but remove any existing fingerprints to avoid any conflict.
* Leave the original code intact as much as possible.

The current handlers available are:

* _Anidea for Ikea Button_ for the **IKEA Tradfri Wireless Shortcut Button**.
* _Anidea for SmartSense Temp/Humidity Sensor_ for the **Sonoff SNZB-02 Zigbee Temperature and Humidity Sensor** (which identifies as the **eWeLink TH01**).
* _Anidea for Zigbee Button_ for the **Sonoff SNZB-01 Zigbee Wireless Switch** (which identifies as the **eWeLink WB01**).
