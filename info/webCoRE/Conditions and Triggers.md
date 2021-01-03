# webCoRE: Conditions and Triggers
_An early trap for novice webCoRe users is the difference between ‘conditions’ and ‘triggers’, which is not helped by the term ‘condition’ being used in two ways.
Even more experienced users may offer incorrect explanations as to how they work or should be used. Including me._

Consider the following conditional block:

```
if
    Door Sensor contact is open
then
    …
end if
```

When adding the condition, the options available are divided into _conditions_ and _triggers_. The above one is a _condition_. How it behaves depends on the event that fired the piston. 

If the piston is handling a `Door Sensor contact` event, the state of the `contact` is determined by the event value. So if the event value is `open`, the condition evaluates as true. 

If the event is received late or processing is delayed, for example by a [semaphore wait](Piston%20waited%20at%20a%20semaphore.md), it is possible for the `contact` to have changed state. So `contact is open` could return true even though the contact is actually closed. Similarly the condition will continue to return true for the duration of the piston instance, regardless of the actual state.

If the piston is handling any other event, the condition works on the current state of the Door Sensor contact attribute (read from the device object). If the contact is open, the comparison evaluates to true, otherwise it is false. Unless the attribute is changed, evaluating the comparison will continue to give the same result.

Now consider this block:

```
if
    Door Sensor contact changes to open
then
    …
end if
```

This time a ‘trigger’ is used. It works on the new state of the Door Sensor contact which is read from the event that fired the piston and so it can only ever evaluate to true if the piston is currently being executed because the contact changed.

It is often stated that, when a piston executes, ‘only one trigger can be true at a time’. That is more or less right. As triggers work using values carried in the event, a trigger can only be true if it is the reason the piston fired. If there are different triggers based on the same device attribute then more than one could evaluate to true.

It is helpful to appreciate that, when the piston is executed, both conditions and triggers are simply comparisons. The data they work with tend to have different sources because the triggers are event oriented, and you do get some special cases like timed triggers, but essentially they are just comparisons returning true or false.

General speaking, the comparisons used to evaluate a device attribute based ‘condition’ will work on the current value, which is read from the device attribute directly, and in the case of the ‘was’ condition, with a list of events read from the device object. The exception is that any value carried in the event being handled is treated as the current value. A device attribute based  ‘trigger’ will work on the new value, which is carried in the event, and may compare it with a cached/previous value. Exactly how the cache is handled isn't clear, but it seems that the trigger needs to be evaluated in order for the cached value to be recorded, so the piston should always reach the trigger that caused it to be running.

Some users will state that triggers are only true for a short period, which is sort of true for the above reason, but this is often a claim made in the context of a misunderstanding of how the [Task Cancellation Policy](Task%20Cancellation%20Policy.md) works.

There is another key difference between ‘conditions’ and ‘triggers’ that is the source of much misunderstanding, and that is to do with triggering the execution of the piston, as was touched on earlier. A piston is essentially an event handler, so it needs some events to make it run. These might be produced within webCoRE itself, which would include the ‘Test’ button in the webCoRE dashboard being pressed, the ‘external URL’ being called, or one piston being executed by another piston. However, most commonly a piston runs because it is subscribed to SmartThings device or location events, or has scheduled timer events.

It is particularly important to understand that pistons do not subscribe to specific attribute changes. For example, a piston might subscribe to changes in the contact attribute of Door Sensor, but it will not specifically subscribe to changes to open and so it has to consider both those possibilities. It should also be understood that a piston has to schedule its next timed execution for itself.

It should be emphasised that there is nothing out there evaluating trigger conditions and firing pistons in response to them. If you have the trigger condition ‘Sensor temperature rises above 30 C’, the piston will start executing (from the top) whenever an event is received with a new value for the Sensor temperature. It doesn’t matter what the value is, and it can not be assumed that the value has actually changed. It is the piston itself that decides what has actually happened. In this case it will check if the piston fired because it received an event carrying a new value for the Sensor temperature and it will look at that value. It will decide if it is over 30 C, and it will look to see if the previous reading it received was 30 C or under. If all that is true then it will return true for the comparison.

So how does the piston decide which events it should be subscribing to, or which times of day it should be running? Well the first place it looks is in the ‘triggers’. It works out what device and location events could affect the result of the comparison and subscribes to them, and it also works out what times of day it needs to run. If any triggers are found, that is pretty much it.

So what happens if there aren’t any ‘triggers’? In that case webCoRE will try to help out by looking at the ‘conditions’ (ALL of them). This is often described informally as ‘treating the conditions as triggers’, or the ‘conditions becoming triggers’. 

This can be very useful behaviour. Consider the following piston:

```
if
    Door Sensor contact changes to open
    and
    Time is between sunrise and sunset
then
    …
end if
```

This piston will only run, by default, when the Door Sensor contact changes, so if it is already open at sunrise nothing gets done. However, consider changing the trigger to a condition:

```
if
    Door Sensor contact is on
    and
    Time is between sunrise and sunset
then
    …
end if
```

Now the piston will, by default, be executed whenever the Door Sensor contact changes, at sunrise, and at sunset. So if it is open at sunrise, the piston will do something. That might be more useful.

The above describes the default Automatic setting for the ‘Subscription Method’, which can be set for each condition or trigger in the piston (the setting is accessed via the cog icon in the condition configuration). The other choices are Never subscribe and Always subscribe. Arguably this is more useful when applied to conditions. Indeed it isn’t obvious what the point would be for triggers..

The triggers and/or conditions that are resulting in subscriptions are usually indicated by ‘lightning’ icons by the line numbers when the piston is viewed in webCoRE (but not in edit mode). ‘Usually’ because you see plenty of screenshots where they aren’t there for some reason (which might, for example, be browser dependent). If you turn up the logs to ‘full’ you will see messages about the piston subscribing to device events when it starts up (which it does when you save an edit or resume a paused piston). The actual subscriptions show as ‘device.attribute’, but the piston will also fake subscriptions for controlled devices under the name of the device alone, which is why, for example, the Classic app can show a device is being used in a particular piston even though it isn’t being subscribed to.

It is the Subscription Method concept that leads to users making comments like the obviously incorrect ‘you need to have a trigger’, or to present ‘triggers before conditions’ as if it is an objective requirement. It might be a good idea in some circumstances, particularly within individual 'if' statements, but it can also be a subjective choice. In some cases, not using explicit triggers may be beneficial.

It is, however, fair to say that some users feel they benefit from having an explicit trigger in every piston, and indeed like to limit themselves to triggers based on a single device attribute. It is also fair to say that, if there are too many subscriptions, the piston can end up being run unnecessarily, and this can not only make the behaviour of the piston less obvious, but it can potentially cause things to break and it leads onto the subject of semaphore waits.

There is an issue that has been observed with triggers using the ‘changes’ comparison. The comparison is between the attribute value in the event, and a cached ‘previous value’ of the attribute. It isn’t clear when the cached value is updated. The unexpected behaviour of some pistons could be explained by the value only updating when the trigger is evaluated, and also by it being updated when the current value is checked. So there could be a lot to be said for making sure triggers are always evaluated when they are the reason for the piston having been fired, and evaluating the triggers before the conditions based on the same attributes.
