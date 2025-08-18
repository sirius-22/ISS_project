%====================================================================================
% gui description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/qak/events").
request( loadrequest, loadrequest(PID) ).
reply( loadaccepted, loadaccepted(SLOT) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON) ).  %%for loadrequest
dispatch( changed, changed(JSONSTATE) ).
event( changed, changed(JSONSTATE) ).
%====================================================================================
context(ctx_cargoservice, "localhost",  "TCP", "8000").
context(ctx_gui, "localhost",  "TCP", "8001").
 qactor( cargoservice, ctx_cargoservice, "external").
  qactor( gui_api_gateway, ctx_gui, "it.unibo.gui_api_gateway.Gui_api_gateway").
 static(gui_api_gateway).
  qactor( gui_state_observer, ctx_gui, "it.unibo.gui_state_observer.Gui_state_observer").
 static(gui_state_observer).
  qactor( gui_request_handler, ctx_gui, "it.unibo.gui_request_handler.Gui_request_handler").
 static(gui_request_handler).
