# webCoRE: Piston waited at a semaphore

Sometimes you see `Piston waited at a semaphore for 10000ms` in the piston logs (the exact figure will vary slightly). As ever with webCoRE, a paucity of documentation
can lead to confusion over what this means.

Sometimes a piston can be fired by two or more events at pretty much the same time. The default behaviour of webCoRE (parallelism NOT enabled) is to try and run instances
of pistons serially. To achieve this, when they start up they will look to see if the piston is already running and, if it is, they will loop for up to ten seconds to 
give it a chance to finish. That is all a semaphore wait is.

_Although the piston code suggests the wait is ‘up to ten seconds’ and clearly seems to be checking something every fraction of a second, most semaphore waits seem to be ten seconds +/- a smidgen. Shorter waits certainly do occur but it isn't obvious why that isn't always the case._

If you see frequent semaphore waits, it might be a sign that a piston is doing something that results in itself being triggered, ‘positive feedback’ as it were. That is worth looking out for. It may also mean too many triggers are firing at once and perhaps the piston could be more efficient. However if nothing actually breaks, a semaphore wait is just normal behaviour and nothing to worry about.

A semaphore wait could cause problems in certain circumstances, though. If something changes while the piston is delayed, the piston may do things you don’t want it to do. For example, you might use a virtual switch as an override to stop lights being controlled by motion, but want to clear this flag if the lights are turned off. So you could be sitting in a room, have the lights go off, immediately activate the override, and then find a delayed piston turning it off again. _The real problem is that the piston has too many trigger events to process. You might be able to solve some problems by enabling parallelism, but there is a lot to be said for having multiple pistons responding to fewer events._

A variation on the above would be a ‘bouncing’ switch. A piston might fire because the switch is turned ‘off’, then a fraction of a second later the switch might bounce back to ‘on’. The piston would fire a second time but enter a semaphore wait because the first instance hasn’t finished. Meanwhile the switch may have settled down to ‘off’ and the piston will fire again. Assuming the first piston has now finished, it seems this instance will see there isn’t a piston running and so immediately execute. So a switch that actually reports ‘off, on, off’ appears to the piston as ‘off, off, on’.
