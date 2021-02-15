/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * ---------------------------------------------------------------------------------
 *
 * Lan MultiThing STT Child
 * ========================
 * This device handler implements a motion sensor device to work with LAN MultiThing.
 *
 * Please be aware that this file is created in the SmartThings Groovy IDE and it may
 * format differently when viewed outside that environment.
 */

def ai_v = '21.02.15.00'
def ai_r = true

metadata
{
	definition (name: "LAN MultiThing Motion Sensor Child" + ( ai_r ? '' : " ${ai_v}" ), namespace: "orangebucket", author: "Graham Johnson")
    {
		capability "Sensor"
        capability "Motion Sensor" 
	}
        
	preferences
	{
	}

	// One day I will investigate this.
	simulator
    {
	}

	tiles
    {        
		standardTile("motion", "device.motion", width: 2, height: 2)
        {
			state "active", label:'active', icon:"st.motion.motion.active", backgroundColor:"#00a0dc"
			state "inactive", label:'inactive', icon:"st.motion.motion.inactive", backgroundColor:"#ffffff"
        }
        
        main "motion"
        // Sort the tiles suitably.
        details (["motion"])
	}
}

def installed()
{
	logger("installed")
    
    updated()
}


// The updated() command is called when preferences are saved. It often seems
// to be called twice so an attempt is made to only let it run once in a five
// second period.
def updated()
{
	if (state.lastupdated && now() < state.lastupdated + 5000)
    {        
        logger("updated", "Skipped as ran recently")
 
 		return
    }
        
 	state.lastupdated = now()

	logger("updated")
}

// Have own logging routine.
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

import groovy.json.JsonSlurper 

def parse(description)
{
	def msg = parseLanMessage(description)
 
	if (msg.body)
    {
     	def jsonSlurper = new JsonSlurper()
    	def body = jsonSlurper.parseText(msg.body)

	   	body.attribute.each
		{
			myname, myvalue ->
                                    
        	if (myname == "motion")
        	{
				logger("parse", "info", "attribute $myname $myvalue")

				sendEvent(name: myname, value: myvalue, isStateChange: true)
			}
        }
	}
}