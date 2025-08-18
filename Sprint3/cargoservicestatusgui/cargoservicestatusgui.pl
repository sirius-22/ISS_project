%====================================================================================
% cargoservicestatusgui description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/qak/events").
request( loadrequest, loadrequest(PID) ).
request( client_loadrequest, client_loadrequest(PID,SESSION_ID) ).
reply( loadaccepted, loadaccepted(SLOT) ).  %%for loadrequest
reply( loadrejected, loadrejected(REASON) ).  %%for loadrequest
dispatch( hold_state_update, hold_state_update(JSONSTATE) ).
dispatch( load_response, response(SESSION_ID,JSON_STRING) ).
%====================================================================================
context(ctx_gui, "localhost",  "TCP", "8001").
context(ctx_cargoservice, "cargoservicecore",  "TCP", "8000").
context(ctx_springboot_gui, "cargoservicestatusgui_model",  "TCP", "8002").
 qactor( cargoservice, ctx_cargoservice, "external").
  qactor( springboot_gui, ctx_springboot_gui, "external").
  qactor( gui_api_gateway, ctx_gui, "it.unibo.gui_api_gateway.Gui_api_gateway").
 static(gui_api_gateway).
  qactor( gui_state_observer, ctx_gui, "it.unibo.gui_state_observer.Gui_state_observer").
 static(gui_state_observer).
  qactor( gui_request_handler, ctx_gui, "it.unibo.gui_request_handler.Gui_request_handler").
 static(gui_request_handler).
