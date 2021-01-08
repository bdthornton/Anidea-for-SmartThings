# webCoRE: Timed triggers (‘stays’)

The various ‘stays’ triggers, which are more formally known as ‘timed triggers’, are rather appealing on the face of it, but their behaviour could be considered
counter-intuitive. Consider the following pseudo-code:

```
if Sensor motion stays inactive for 3 minutes
then
  turn off light
else
  do something else
endif
do some other stuff 
```

Here are some ways it doesn't work:

* It is not a simple condition that tests if the motion has been inactive for three minutes. There is such a condition (‘was’) but this isn't it.
* It is not a trigger that looks to see if motion has changed to active having previously been inactive for at least three minutes.
* The piston does not get fired when motion has already been inactive for three minutes. That isn't how triggers work.
* The piston will not simply sit around for up to three minutes to see if inactive motion remains that way, either as a condition or a trigger.

So how does it work?

Firstly the named ‘timed trigger’ suggests it is a trigger. It certainly is in the way that it automatically subscribes to the motion. However the comparison it uses
is actually the same as ‘is inactive’.

If motion is not inactive the comparison just returns false and it will ‘do something else’ and ‘do some other stuff’.

Now supposing motion is inactive. In this case two things will happen, both a bit different from a simple condition or trigger.

* A scheduled wake up will be set for three minutes time, rather like a condition returning true and there being a ‘wait 3 minutes’ at the beginning of the 'then' block.
The condition will immediately return false and so the piston will ‘do something else’ and ‘do some other stuff’.

* The wake up side of things works pretty much like a typical ‘wait 3 minutes; turn off light’. If the piston doesn’t fire again inside three minutes, the wait will finish, the piston will wake up, a light is turned off, and then it will ‘do some other stuff’. If it does fire and the condition is still true (the condition evaluates like ‘is inactive’ and NOT ‘changes to inactive’) it will be left alone. If it does fire and the motion is now inactive, the scheduled task is cancelled (the piston wakes up but does nothing), the piston ‘will do something else’ and then ‘do some other stuff’.

It is something like:

```
if Sensor motion is inactive (set to always subscribe)
then
  wait 3 minutes
  turn off light
else
  do something else
endif
do some other stuff
```

The difference is that it acts as a trigger the first time around and the comparison effectively returns BOTH true and false.

*This is, of course, a simple example, with the interpretation above based on equivalent results rather than the actual behaviour. When it comes to more complex examples, 
perhaps with ‘stays’ being one of a number of comparisons in a conditional group, the behaviour has to be looked at more precisely.*

*The piston is effectively waiting for up to three minutes to decide if the ‘stays’ comparison should return true and it should continue. However it is doing that 
asynchronously and in the meantime it is returning false.*
