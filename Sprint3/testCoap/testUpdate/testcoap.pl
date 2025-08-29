%====================================================================================
% testcoap description   
%====================================================================================
mqttBroker("127.0.0.1", "1883", "unibo/qak/events").
dispatch( changed, changed(ARG) ).
%====================================================================================
context(ctx_update, "localhost",  "TCP", "8100").
 qactor( updater, ctx_update, "it.unibo.updater.Updater").
 static(updater).
  qactor( local_observer, ctx_update, "it.unibo.local_observer.Local_observer").
 static(local_observer).
