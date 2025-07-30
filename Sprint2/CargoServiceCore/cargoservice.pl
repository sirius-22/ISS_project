%====================================================================================
% cargoservice description   
%====================================================================================
request( loadrequest, loadrequest(PID) ).
request( getProduct, product(PID) ).
reply( getProductAnswer, product(JSonString) ).  %%for getProduct
reply( loadaccepted, loadaccepted(Slot) ).  %%for loadrequest
reply( loadrejected, loadrejected(Error) ).  %%for loadrequest
dispatch( restart, restart(M) ).
request( loadcontainer, loadcontainer(Slot) ).
reply( containerloaded, containerloaded(M) ).  %%for loadcontainer
dispatch( cmd, cmd(M) ).
event( alarm, alarm(STOP) ).
request( engage, engage(CALLER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
dispatch( setdirection, dir(D) ).
dispatch( setrobotstate, setpos(X,Y,Dir) ).
request( doplan, doplan(PATH,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
event( stopActions, stopActions(M) ).
event( resumeActions, resumeActions(M) ).
event( containerhere, containerhere(M) ).
dispatch( sonaractivate, sonaractivate(A) ).
dispatch( resume, resume(M) ).
dispatch( goto_idle, goto_idle(M) ).
%====================================================================================
context(ctx_cargoservice, "localhost",  "TCP", "8000").
context(ctx_basicrobot, "basicrobot24",  "TCP", "8020").
context(ctx_productservice, "cargoserviceqak",  "TCP", "8111").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( productservice, ctx_productservice, "external").
  qactor( sonar_mock, ctx_cargoservice, "it.unibo.sonar_mock.Sonar_mock").
 static(sonar_mock).
  qactor( cargorobot, ctx_cargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( cargoservice, ctx_cargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( external_client, ctx_cargoservice, "it.unibo.external_client.External_client").
 static(external_client).
