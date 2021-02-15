/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * ---------------------------------------------------------------------------------
 *
 * Anidea for Security System Helper
 * =================================
 * This DTH implements a helper device to assist in setting and exposing the
 * location security system outside of the mobile apps, Automations and the Rules
 * API.  The arming and disarming side of things is handled using three child 
 * devices, one for each security state, that use the 'Anidea for Virtual Button'
 * handler to provide Button and Momentary capabilities.  The status side of things
 * uses the Notification capability as a 'stock' way of allowing the attribute of a
 * custom capability to be set to one of the security states (custom capabilities
 * were not supported in Automations at the time).
 *
 * The actual arming/disarming of the security system, and the updating of the 
 * status from the security system status, requires external automations.
 */

def ai_v = '21.02.15.00'
def ai_r = true

metadata
{
	definition( name: 'Anidea for Security System Helper' + ( ai_r ? '' : " ${ai_v}" ), namespace: 'orangebucket', author: 'Graham Johnson', mcdSync: true,
    			ocfDeviceType: 'oic.wk.d', mnmn: 'SmartThingsCommunity', vid: 'f32b4796-d5f6-35ce-ab7a-8a6f29f4ee4e' )
    {
        //
		capability 'circlemusic21301.sthmStatus'
        capability 'Notification'
        // 
		capability 'Health Check'
        //
        capability 'Actuator'
        capability 'Sensor'
	}

	preferences
    {
    	// No preferences are actually needed.
	}
}

// installed() is called when the device is created, and when the device is updated in the IDE.
def installed()
{	
	logger( 'installed', 'info', '' )

    // Health Check is undocumented but this seems to be the common way of creating an untracked
    // device that will appear online when the hub is up.
	sendEvent( name: 'DeviceWatch-Enroll', value: [ protocol: 'cloud', scheme: 'untracked' ].encodeAsJson(), displayed: false )

	// The status needs to be initialised to keep the mobile app happy. Setting it to disarmed seems the best choice.
    sendEvent( name: 'sthmStatus', value: 'disarmed', displayed: false )
    
    state.lastlabel = device.displayName

    def buttons = [ disarm: 'Disarm', stay: 'Arm (Stay)', away: 'Arm (Away)' ]
    
    if ( ! childDevices ) buttons.each
    {
    	shortform, longform -> 
    	
        logger( 'installed', 'debug', "${longform} [${shortform}]" )

		// Probably ought to check for existing child devices.
    	def child = addChildDevice( 'Anidea for Virtual Button', "${device.deviceNetworkId}:${shortform}", device.hubId,
				    [ isComponent: true, completedSetup: true, label: "${device.displayName} ${longform}", componentName: shortform, componentLabel: longform ] )
    }
}

// updated() seems to be called after installed() when the device is first installed, but not when
// it is updated in the IDE without there having been any actual changes.  It runs whenever settings
// are updated in the mobile app. It often used to be seen running twice in quick succession so was
// debounced in many handlers.
def updated()
{
	logger( 'updated', 'info', '' )
    
    def buttons = [ disarm: 'Disarm', stay: 'Arm (Stay)', away: 'Arm (Away)' ]
    
    if ( childDevices && device.displayName != state.lastlabel) 
    {
		childDevices.each
        {
			def longform = buttons[ it.deviceNetworkId.split(":")[-1] ]
			it.setLabel( "${device.displayName} ${longform}" )
		}
        
		state.lastlabel = device.displayName
	}
}

def logger( method, level = 'debug', message = '' )
{
	// Using log."${level}" for dynamic method invocation is now deprecated.
    switch( level )
	{
		case 'info':	log.info  "$device.displayName [$device.name] [${method}] ${message}"
        				break
        default:	    log.debug "$device.displayName [$device.name] [${method}] ${message}"
        				break
	}
}

def deviceNotification( status )
{
	logger( 'deviceNotification', 'info', status )
    
	sendEvent( name: 'sthmStatus', value: status )
}

// parse() is called when the hub receives a message from a device.
def parse( String description )
{
    logger( 'parse', 'debug', description )
    
	// Nothing should appear.
}
