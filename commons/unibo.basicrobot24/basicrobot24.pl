%====================================================================================
% basicrobot24 description   
%====================================================================================
mqttBroker("localhost", "1883", "robotevents").
dispatch( cmd, cmd(MOVE) ).
dispatch( end, end(ARG) ).
request( step, step(TIME,PLANID) ).
reply( stepdone, stepdone(V,PLANID) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE,PLANID) ).  %%for step
reply( stepcollided, stepcollided(DURATION,PLANID) ).  %%for step
request( stepback, stepback(TIME,PLANID) ).
reply( stepbackdone, stepbackdone(V,PLANID) ).  %%for stepback
reply( stepbackfailed, stepbackfailed(DURATION,CAUSE,PLANID) ).  %%for stepback
reply( stepbackcollided, stepbackcollided(DURATION,PLANID) ).  %%for stepback
event( sonardata, sonar(DISTANCE) ).
event( obstacle, obstacle(X) ).
event( info, info(X) ).
request( doplan, doplan(PATH,STEPTIME,PLANID) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(PATH_DONE,PATH_TODO) ).  %%for doplan
dispatch( setrobotstate, setpos(X,Y,D) ).
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
request( checkowner, checkowner(CALLER) ).
reply( checkownerok, checkownerok(ARG) ).  %%for checkowner
reply( checkownerfailed, checkownerfailed(ARG) ).  %%for checkowner
event( alarm, alarm(X) ).
dispatch( nextmove, nextmove(M) ).
dispatch( nomoremove, nomoremove(M) ).
dispatch( setdirection, dir(D) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(NoOwner) ).  %%for moverobot
request( getrobotstate, getrobotstate(ARG) ).
reply( robotstate, robotstate(POS,DIR) ).  %%for getrobotstate
request( getenvmap, getenvmap(X) ).
reply( envmap, envmap(MAP) ).  %%for getenvmap
dispatch( updatemappath, path(MOVES) ).
dispatch( resume, resume(Goon) ).
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( engager, ctxbasicrobot, "it.unibo.engager.Engager").
 static(engager).
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
 static(basicrobot).
  qactor( planexec, ctxbasicrobot, "it.unibo.planexec.Planexec").
 static(planexec).
  qactor( robotpos, ctxbasicrobot, "it.unibo.robotpos.Robotpos").
 static(robotpos).
