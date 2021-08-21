/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * ---------------------------------------------------------------------------------
 *
 * Anidea for Virtual Binary
 * =========================
 * This device handler implements a virtual binary state device with the option of
 * independent attribute control or all attributes linked.
 */
 
def ai_v = "21.08.21.01"
def ai_r = true

metadata
{
	definition( name: 'Anidea for Virtual Binary' + ( ai_r ? '' : " ${ai_v}" ), namespace: 'orangebucket', author: 'Graham Johnson',
    			ocfDeviceType: 'oic.wk.d', mnmn: 'SmartThingsCommunity', vid: 'a72d78a6-2c3b-3b42-9970-fa67a4c3750d' )
    {
        capability 'Contact Sensor'
        capability 'Motion Sensor'
        capability 'Occupancy Sensor'
        capability 'Presence Sensor'
        capability 'Switch'
        capability 'Water Sensor'
        //
		capability 'Health Check'
        //
        capability 'Actuator'
		capability 'Sensor'

		// Add a 'binary' attribute with values 'active' and 'inactive'
        // and setter commands 'binaryactive' and 'binaryinactive'.
     	capability 'circlemusic21301.binarySensor'

		// Add basic commands to set sensors using custom capabilities.
        capability 'circlemusic21301.contactCommands'
        capability 'circlemusic21301.motionCommands'
        capability 'circlemusic21301.occupancyCommands'
        capability 'circlemusic21301.presenceCommands'
        capability 'circlemusic21301.waterCommands'
        
        // Add an attribute to report currently enabled sensors.
        capability 'circlemusic21301.selectedSensors'
	}

	preferences
    {
        input name: 'linkedsensors',   	type: 'bool', title: 'Link Sensors?',   				 description: 'Enter boolean', required: true

        input name: 'virtualcontact',   type: 'bool', title: 'Act as virtual Contact Sensor?',   description: 'Enter boolean', required: true
        input name: 'virtualmotion',    type: 'bool', title: 'Act as virtual Motion Sensor?',    description: 'Enter boolean', required: true
        input name: 'virtualoccupancy', type: 'bool', title: 'Act as virtual Occupancy Sensor?', description: 'Enter boolean', required: true
        input name: 'virtualpresence',  type: 'bool', title: 'Act as virtual Presence Sensor?',  description: 'Enter boolean', required: true
        input name: 'virtualwater',     type: 'bool', title: 'Act as virtual Water Sensor?',     description: 'Enter boolean', required: true
	}
}

// installed() is called when the device is created, and when the device is updated in the IDE.
def installed()
{	
	logger( 'installed', 'info', '' )

    // Health Check is undocumented but this seems to be the common way of creating an untracked
    // device that will appear online when the hub is up.
	sendEvent( name: 'DeviceWatch-Enroll', value: [protocol: 'cloud', scheme:'untracked'].encodeAsJson(), displayed: false )
    
    sendEvent( name: 'binary', 	  value: 'inactive', 	 displayed: false )
    sendEvent( name: 'contact',   value: 'closed',       displayed: false )
    sendEvent( name: 'motion',    value: 'inactive',     displayed: false )
    sendEvent( name: 'occupancy', value: 'unoccupied',   displayed: false )
    sendEvent( name: 'presence',  value: 'not present',  displayed: false )
    sendEvent( name: 'switch',    value: 'off',          displayed: false )
    sendEvent( name: 'water',	  value: 'dry',			 displayed: false )
    
    sendEvent( name: 'selectedSensors', value: 'Switch', displayed: false )
}

// updated() seems to be called after installed() when the device is first installed, but not when
// it is updated in the IDE without there having been any actual changes.  It runs whenever settings
// are updated in the mobile app. It often used to be seen running twice in quick succession so was
// debounced in many handlers.
def updated()
{
	logger( 'updated', 'info', '' )

	def selectedsensors = 'Switch'
    
    logger( 'updated', 'debug', 'Linking of all sensors '   + ( linkedsensors    ? 'enabled' : 'disabled' ) )
    
    logger( 'updated', 'debug', 'Virtual Contact Sensor '   + ( virtualcontact   ? 'enabled' : 'disabled' ) )
    logger( 'updated', 'debug', 'Virtual Motion Sensor '    + ( virtualmotion    ? 'enabled' : 'disabled' ) )
    logger( 'updated', 'debug', 'Virtual Occupancy Sensor ' + ( virtualoccupancy ? 'enabled' : 'disabled' ) )
    logger( 'updated', 'debug', 'Virtual Presence Sensor '  + ( virtualpresence  ? 'enabled' : 'disabled' ) )
    logger( 'updated', 'debug', 'Virtual Water Sensor '     + ( virtualwater     ? 'enabled' : 'disabled' ) )
    
    selectedsensors += virtualcontact   ? ' Contact'   : ''
    selectedsensors += virtualmotion    ? ' Motion'    : ''
    selectedsensors += virtualoccupancy ? ' Occupancy' : ''
    selectedsensors += virtualpresence  ? ' Presence'  : ''
    selectedsensors += virtualwater     ? ' Water'     : ''
    
    sendEvent( name: 'selectedSensors', value: selectedsensors )
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

def on()
{
	logger( 'on', 'info', '' )
    
    linkedsensors ? binaryactive() : sendEvent( name: 'switch', value: 'on' )
}

def off()
{
	logger( 'off', 'info', '' )
    
    linkedsensors ? binaryinactive() : sendEvent( name: 'switch', value: 'off' )
}

// parse() is called when the hub receives a message from a device.
def parse( String description )
{
    logger( 'parse', 'debug', description )
    
	// Nothing should appear.
}

def binaryactive()
{
	logger( 'binaryactive', 'info', '' )
    
    sendEvent( name: 'binary', value: 'active' )
    
    // Change attributes to the active state.
    if ( linkedsensors && virtualcontact   ) sendEvent( name: 'contact',   value: 'open'     )
    if ( linkedsensors && virtualmotion    ) sendEvent( name: 'motion',    value: 'active'   )
    if ( linkedsensors && virtualoccupancy ) sendEvent( name: 'occupancy', value: 'occupied' )
    if ( linkedsensors && virtualpresence  ) sendEvent( name: 'presence',  value: 'present'  )
    if ( linkedsensors                     ) sendEvent( name: 'switch',    value: 'on'       )
    if ( linkedsensors && virtualwater     ) sendEvent( name: 'water',     value: 'wet'      )
}

def binaryinactive()
{
	logger( 'binaryinactive', 'info', '' )
    
    sendEvent( name: 'binary', value: 'inactive' )
    
	// Return attributes to inactive state.
	if ( linkedsensors && virtualcontact   ) sendEvent( name: 'contact',   value: 'closed'      )
    if ( linkedsensors && virtualmotion    ) sendEvent( name: 'motion',    value: 'inactive'    )
    if ( linkedsensors && virtualoccupancy ) sendEvent( name: 'occupancy', value: 'unoccupied'  )
    if ( linkedsensors && virtualpresence  ) sendEvent( name: 'presence',  value: 'not present' )
    if ( linkedsensors   			       ) sendEvent( name: 'switch',    value: 'off'         )
    if ( linkedsensors && virtualwater     ) sendEvent( name: 'water',     value: 'dry'         )
}

def open()
{
	logger( 'open', 'info', '' )
    
    linkedsensors ? binaryactive() : sendEvent( name: 'contact', value: 'open' )
}

def close()
{
	logger( 'close', 'info', '' )
    
    linkedsensors ? binaryinactive() : sendEvent( name: 'contact', value: 'closed' )
}

def active()
{
	logger( 'active', 'info', '' )
    
    linkedsensors ? binaryactive() : sendEvent( name: 'motion', value: 'active' )
}

def inactive()
{
	logger( 'inactive', 'info', '' )
    
    linkedsensors ? binaryinactive() : sendEvent( name: 'motion', value: 'inactive' )
}

def occupied()
{
	logger( 'occupied', 'info', '' )
    
    linkedsensors ? binaryactive() : sendEvent( name: 'occupancy', value: 'occupied' )
}

def unoccupied()
{
	logger( 'unoccupied', 'info', '' )
    
    linkedsensors ? binaryinactive() : sendEvent( name: 'occupancy', value: 'unoccupied' )
}

def arrived()
{
	logger( 'arrived', 'info', '' )
    
    linkedsensors ? binaryactive() : sendEvent( name: 'presence', value: 'present' )
}

def departed()
{
	logger( 'departed', 'info', '' )
    
    linkedsensors ? binaryinactive() : sendEvent( name: 'presence', value: 'not present' )
}

def wet()
{
	logger( 'wet', 'info', '' )
    
    linkedsensors ? binaryactive() : sendEvent( name: 'water', value: 'wet' )
}

def dry()
{
	logger( 'dry', 'info', '' )
    
    linkedsensors ? binaryinactive() : sendEvent( name: 'water', value: 'dry' )
}