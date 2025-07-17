%====================================================================================
% logic_model description   
%====================================================================================
request( registrationrequest, registrationrequest(Weight) ).
reply( registrationaccepted, registrationaccepted(PID) ).  %%for registrationrequest
request( loadrequest, loadrequest(PID) ).
request( productdatareq, productdatareq(PID) ).
reply( productdata, productdata(Weight) ).  %%for productdatareq
reply( errorproductdata, errorproductdata(M) ).  %%for productdatareq
reply( loadaccepted, loadaccepted(Slot) ).  %%for loadrequest
reply( loadrejected, loadrejected(Error) ).  %%for loadrequest
request( freeSlot, freeSlot(M) ).
request( totalWeightReq, totalWeightReq(M) ).
reply( slotname, slotname(Slot) ).  %%for freeSlot
reply( totalWeight, totalWeight(Weight) ).  %%for totalWeightReq
request( loadcontainer, loadcontainer(Slot) ).
reply( containerloaded, containerloaded(M) ).  %%for loadcontainer
dispatch( cmd, cmd(M) ).
request( step, step(M) ).
event( alarm, alarm(STOP) ).
event( stopActions, stopActions(M) ).
event( resumeActions, resumeActions(M) ).
event( containerhere, containerhere(M) ).
%====================================================================================
context(ctx_cargoservice, "localhost",  "TCP", "11800").
context(ctx_productservice, "localhost",  "TCP", "11801").
context(ctx_client_simulator, "localhost",  "TCP", "11803").
context(ctx_basicrobot, "localhost",  "TCP", "11810").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( productservice, ctx_productservice, "external").
  qactor( client_simulator, ctx_client_simulator, "it.unibo.client_simulator.Client_simulator").
 static(client_simulator).
  qactor( sonar_mock, ctx_cargoservice, "it.unibo.sonar_mock.Sonar_mock").
 static(sonar_mock).
  qactor( slotmanagement_mock, ctx_cargoservice, "it.unibo.slotmanagement_mock.Slotmanagement_mock").
 static(slotmanagement_mock).
  qactor( cargorobot, ctx_cargoservice, "it.unibo.cargorobot.Cargorobot").
 static(cargorobot).
  qactor( cargoservice, ctx_cargoservice, "it.unibo.cargoservice.Cargoservice").
 static(cargoservice).
