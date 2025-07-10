%====================================================================================
% system_overview description   
%====================================================================================
request( registrationrequest, registrationrequest(Weight) ).
request( loadrequest, loadrequest(PID) ).
request( productdatareq, productdatareq(PID) ).
dispatch( movecontainer, movecontainer(Slot) ).
dispatch( stop, stop(M) ).
dispatch( resume, resume(M) ).
dispatch( cmd, cmd(M) ).
request( step, step(M) ).
dispatch( updategui, updategui(robotState,robotPosition,slotsState,ledState) ).
dispatch( updateled, updateled(LedStatus) ).
dispatch( distance, distance(D) ).
%====================================================================================
context(ctx_cargoservice, "localhost",  "TCP", "11800").
context(ctx_productservice, "localhost",  "TCP", "11801").
context(ctx_raspdevice, "localhost",  "TCP", "11802").
context(ctx_client_simulator, "localhost",  "TCP", "11803").
context(ctx_basicrobot, "localhost",  "TCP", "11810").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( productservice, ctx_productservice, "external").
  qactor( client_simulator, ctx_client_simulator, "it.unibo.client_simulator.Client_simulator").
 static(client_simulator).
  qactor( cargoservicestatusgui, ctx_cargoservice, "it.unibo.cargoservicestatusgui.Cargoservicestatusgui").
 static(cargoservicestatusgui).
  qactor( cargorobot, ctx_cargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( cargoservice, ctx_cargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
  qactor( sonardevice, ctx_raspdevice, "it.unibo.sonardevice.Sonardevice").
 static(sonardevice).
  qactor( leddevice, ctx_raspdevice, "it.unibo.leddevice.Leddevice").
 static(leddevice).
