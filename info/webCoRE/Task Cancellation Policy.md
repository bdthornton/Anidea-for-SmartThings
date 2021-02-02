# webCoRE: Task Cancellation Policy

The _Task Cancellation Policy (TCP)_ frequently traps novice webCoRE users, but is also misunderstood by some more experienced users. This discussion
currently only considers the default TCP setting `Cancel tasks on condition state change` and the commonly used alternative of `Never cancel tasks`.

_I do not consider myself exempt from misunderstanding. Up until the end of November 2019, I had got it into my head that, whenever a piston was
fired during a long wait while it wasn’t actually executing, the existing scheduled task was cancelled and then rescheduled if it was still
the next timed event due. The critical point was that I was also including the actual timer in this thinking. I can only put that down to
forgetting how it actually worked as I doubt things have changed. Actually the timer is never cancelled (that might not even be possible) 
but it may not have anything to do when it wakes up. So when the logs talk of a task being scheduled that means there is an active timer
and something to do. An absence of such a message doesn't mean there isn't still an active timer. Unfortunately I had started using the
piston waking up as a shortcut in diagnosis instead of making sure it did something. But anyway ..._

Consider a simple piston like:

```
if 
    Door Sensor contact changes to open
then 
    with
        Hall Light
    do
        turn on
        wait 10 minutes
        turn off
    end with
end if
```

If webCoRE is using default settings, that piston will be triggered by any change in the contact attribute of the `Door Sensor` and it will turn on the `Hall Light`.
It will then turn off the `Hall Light` ten minutes later unless something happens that will stop it.

The piston sees the comparison as a _trigger_ and subscribes to the `contact` attribute of the `Door Sensor`. If that attribute changes, the piston is run
from the top and comes to the comparison. If the `Door Sensor contact` has just opened, the comparison evaluates to true (`changes to` is implemented by
comparing the value in the event with one in a cache) and the then block is executed. The `Hall Light` is turned on and then the piston schedules a new run
in ten minutes time and exits (if the wait period was less than about five seconds the piston would have simply paused and continued). When the piston is
executed by the scheduler, it 'fast forwards' and continues executing after the wait.

_It used to be common to find users would would claim that in fact the `Hall Light` would not turn off. They suggested that either: after a brief period the
`changes to` comparison no longer evaluated to true and the wait was cancelled; or when the piston resumed after the wait the comparison was evaluated again 
and was no longer true. Either way the task would be cancelled due to the TCP and the light would not be turned off. Not so. The light is turned off._

_Fortunately this misunderstanding of how it works often still led to correct or harmless changes to the default TCP setting being made._

Where the TCP actually comes into play is when the piston runs again from top to bottom. The classic reason for this would be the door being closed (but it could be the ‘test’ button being pressed and in more complex pistons there could be other triggers). As the comparison no longer evaluates to true, webCoRE will assume any tasks previously scheduled as a result of it are no longer of interest and will be cancelled. The light will not be turned off.

When the `Door Sensor contact` closes, the piston will be triggered and queued for execution. As webCoRE defaults to sequential execution, it will look to see if the piston is currently executing. In the example piston, it is highly unlikely that it would be (a ‘wait’ of a few seconds that was implemented as a pause would be different), but a more complex piston might have been. In that case the new piston execution will loop for ten seconds to give the existing run a chance to finish (this is why the logs can show certain pistons having [waited at a semaphore](Piston%20waited%20at%20a%20semaphore.md) for ten seconds). When the piston runs it starts from the top and when it comes to the if condition it now sees it evaluates to false and cancels the scheduled task on the assumption that it is no longer required (this is the default TCP setting `Cancel tasks on condition change`). The timer event will still happen but the piston will not have anything to do when it does.

Now consider this piston instead:

```
if 
    Door Sensor contact changes to open
then 
    if
        Time is between sunrise and sunset
    then
        with
            Hall Light
        do
            turn on
            wait 10 minutes
            turn off
        end with
    end if
end if
```

If the `Door Sensor contact` changes to closed during the wait, the light will not turn off.

_I had always assumed this would be the case but tested it and convinced myself that actually the light did turn off. I can only think I was being lazy and looking for wake ups rather than actual activity. I assume when scheduled tasks are cancelled on the first 'if', those in the second 'if' are also picked up._

The TCP can be changed if required, with `Never cancel tasks` being the most commonly used option. This prevents the scheduled wake-up of the previous execution being cancelled. The TCP can be applied on the if, with, or do (wait) statements (the setting is revealed by clicking on the cog icon while editing the statement). Any advantages of one over the other, or any interactions, are yet to be fully established. One could speculate that the TCP applies to any wait it includes, whereas that on a do (wait) just affects that one action. The TCP on the if remains a bit of a mystery. It certainly doesn't seem to apply to the then block as a whole.

