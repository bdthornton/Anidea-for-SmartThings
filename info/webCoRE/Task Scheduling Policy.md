# webCoRE: Task Scheduling Policy (TSP)

The Task Cancellation Policy is something that new users are likely to quickly bump into, but it has a couple of friends. One of them is the Task Scheduling Policy (TSP).

Consider a simple piston like:

```
if 
    Door Sensor contact changes to open
then 
    with (N)
        Hall Light
    do
        Turn on
        wait 10 minutes
        Turn off
    end with
end if
```

The (N) should be read to indicate that the *Task Cancellation Policy* has been set to `Never cancel tasks`. This means that when the door opens, the light will be turned on, 
and ten minutes later it will be turned off again, even if the door has been closed in the meantime. But what happens if the door is opened again during the wait period?

What doesn’t happen is that the light does not get turned off ten minutes after the door was first opened. Instead the light is turned off ten minutes after it was last opened.

The piston runs from the top every time the door is opened. When it reaches the wait, any existing schedule is overwritten because of the default *Task Scheduling Policy (TSP)*
which is `Override any existing scheduled tasks`.

The *TSP* has an alternative value which is `Allow multiple scheduled tasks`. The documented example where that would be used is in a piston like the following:

```
if 
    Any of Front Light’s or Back Lights’s switch changes to open
then 
	with (N)
        {currentEventDevice}
    do
        wait 10 minutes
        Turn off
    end with
end if
```

As it stands, if the `Front Light` turns on first, a timer will be set to wake up in ten minutes and turn it off. However, if the `Back Light` also turns on during that
time, the timer gets overwritten because of the default TSP, so now the `Back Light` will be turned off after ten minutes and the `Front Light` will stay on.

If the TSP on the with is changed to `Allow multiple scheduled tasks`, there will still be a timer to turn off the `Front Light` ten minutes after it turned on, and another one to turn off the `Back Light` ten minutes after that light turned on.

Note that the TCP has to be set to `Never cancel tasks` as, otherwise, if one of the lights is turned off, all of the schedules for the task will be cancelled.
