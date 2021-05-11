/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * ---------------------------------------------------------------------------------
 *
 * Anidea for Virtual Presence
 * ===========================
 * A virtual presence and occupancy sensor that can handle the two capabilities separately
 * or link them together. Custom commands use standard names where available.
 */

def ai_v = '21.05.11.00'
def ai_r = true

metadata 
{
    definition ( name: 'Anidea for Virtual Presence'  + ( ai_r ? '' : " ${ai_v}" ), namespace: 'orangebucket', author: 'Graham Johnson',
    			 mnmn: 'SmartThingsCommunity', vid: 'e62d46e4-450e-3166-9eb4-d0cb3d74b9d5' ) 
    {
    	// The DTH can work as both a Presence Sensor and an Occupancy Sensor.  The two
        // capabilities are not linked within the handler.
        capability 'Presence Sensor'
        capability 'Occupancy Sensor'
        
        capability 'Sensor'
        capability 'Health Check'

		// Custom commands for setting the presence, named for compatibility with
        // the stock Simulated Presence Sensor DTH as a de facto standard.
        capability 'circlemusic21301.presenceCommands'

		// Custom commands for setting occupancy.  No known standards to follow.
		capability 'circlemusic21301.occupancyCommands'
    }
    
    preferences
    {
    	input name: 'linksensors',   type: 'bool', title: 'Link Presence and Occupancy Sensors?',   description: 'Enter boolean', required: true
    }
}

def installed()
{
    logger( 'installed', 'info', '' )
    
    // Set up Health Check using the untracked enrollment method, copied from stock handlers.
    sendEvent( name: 'DeviceWatch-Enroll', value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false ) 

	// Initialise the attributes to keep the SmartThings app happy. Set the defaults
    // to the values most likely to keep things secure.
    sendEvent( name: 'presence',  value: 'not present', displayed: false )
    sendEvent( name: 'occupancy', value: 'unoccupied',  displayed: false )
}

def updated() 
{
    logger( 'updated', 'info', '' )
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

// This should be redundant.
def parse( description )
{
    logger( 'parse', 'info', description )
}

def arrived() 
{
    logger( 'arrived', 'info', '' )
    
    sendEvent( name: 'presence', value: 'present' )
    
    if ( linksensors ) sendEvent( name: 'occupancy', value: 'occupied' )
}

def departed() 
{
    logger( 'departed', 'info', '' )
    
    if ( linksensors ) sendEvent( name: 'occupancy', value: 'not occupied' )
    
    sendEvent( name: 'presence', value: 'not present' )
}

def occupied() 
{
    logger( 'occupied', 'info', '' )
    
    if ( linksensors ) sendEvent( name: 'presence', value: 'present' )
    
    sendEvent( name: 'occupancy', value: 'occupied' )
}

def unoccupied() 
{
    logger( 'unoccupied', 'info', '' )
    
    sendEvent( name: 'occupancy', value: 'unoccupied' )
    
    if ( linksensors ) sendEvent( name: 'presence', value: 'not present' )
}