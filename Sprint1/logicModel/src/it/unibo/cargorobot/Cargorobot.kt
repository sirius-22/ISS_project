/* Generated by AN DISI Unibo */ 
package it.unibo.cargorobot

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
//Sept2024
import org.slf4j.Logger
import org.slf4j.LoggerFactory 
import org.json.simple.parser.JSONParser
import org.json.simple.JSONObject


//User imports JAN2024

class Cargorobot ( name: String, scope: CoroutineScope, isconfined: Boolean=false, isdynamic: Boolean=false ) : 
          ActorBasicFsm( name, scope, confined=isconfined, dynamically=isdynamic ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		//IF actor.withobj !== null val actor.withobj.name» = actor.withobj.method»ENDIF
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						subscribeToLocalActor("sonar_mock") 
						subscribeToLocalActor("sonar_mock") 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_engage", cond=doswitch() )
				}	 
				state("state_engage") { //this:State
					action { //it:State
						delay(3000) 
						request("engage", "engage("cargorobot")" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t13",targetState="state_idle",cond=whenReply("engagedone"))
					transition(edgeName="t14",targetState="state_engage",cond=whenReply("engagerefused"))
				}	 
				state("state_idle") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="state_move_cont",cond=whenRequest("loadcontainer"))
					interrupthandle(edgeName="t06",targetState="state_wait_resume",cond=whenEvent("stopActions"),interruptedStateTransitions)
				}	 
				state("state_wait_resume") { //this:State
					action { //it:State
						emit("alarm", "alarm(STOP)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t17",targetState="state_resume",cond=whenEvent("resumeActions"))
				}	 
				state("state_resume") { //this:State
					action { //it:State
						returnFromInterrupt(interruptedStateTransitions)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_move_cont") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("loadcontainer(Slot)"), Term.createTerm("movecontainer(Slot)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var Slot =  payloadArg(0)  
						}
						CommUtils.outblack("[Cargorobot] Moving container to slot $Slot...")
						request("moverobot", "moverobot(MOVES)" ,"basicrobot" )  
						CommUtils.outblack("[cargoRobot] container transported")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t38",targetState="returnHOME",cond=whenReply("moverobotdone"))
					transition(edgeName="t39",targetState="goto_IO_port",cond=whenReply("moverobotfailed"))
				}	 
				state("returnHOME") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("goto_IO_port") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_move_cont", cond=doswitch() )
				}	 
			}
		}
} 
