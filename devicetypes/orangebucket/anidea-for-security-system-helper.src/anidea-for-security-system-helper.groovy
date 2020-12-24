/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose
 * with or without fee is hereby granted, provided that the copyright notice below
 * and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH 
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER 
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * ---------------------------------------------------------------------------------
 *
 * Anidea for Security System Helper
 * =================================
 * Version:	 20.10.29.00
 */

metadata
{
	definition( name: 'Anidea for Security System Helper', namespace: 'orangebucket', author: 'Graham Johnson', mcdSync: true,
    			ocfDeviceType: 'oic.wk.d', mnmn: 'SmartThingsCommunity', vid: 'a33ccab9-cdcc-3a66-ac73-a079035e85ee' )
    {
        //
        capability 'Notification'
        //
		capability 'circlemusic21301.sthmStatus'
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
