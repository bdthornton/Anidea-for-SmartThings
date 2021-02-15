/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * ---------------------------------------------------------------------------------
 *
 * Lan MultiThing STT Child
 * ========================
 * This device handler implements a child Speech Recognition (STT) device to work
 * with LAN MultiThing. The Speech Recognition capability only has an attribute so
 * could be used for generic text.
 *
 * Please be aware that this file is created in the SmartThings Groovy IDE and it may
 * format differently when viewed outside that environment.
 */
 
def ai_v = '21.02.15.00'
def ai_r = true

metadata
{
	definition (name: "LAN MultiThing STT Child" + ( ai_r ? '' : " ${ai_v}" ), namespace: "orangebucket", author: "Graham Johnson")
    {
		capability "Sensor"
        capability "Speech Recognition" 
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
        valueTile("sst", "device.phraseSpoken", decoration: "flat", width: 3, height:1)
        {
        	state "phraseSpoken", label:'${currentValue}'
    	} 
        
        main "sst"
        // Sort the tiles suitably.
        details (["sst"])
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
                                    
        	if (myname == "phraseSpoken")
        	{
				logger("parse", "info", "attribute $myname $myvalue")

				sendEvent(name: myname, value: myvalue, isStateChange: true)
			}
        }
	}
}