%====================================================================================
% testcoap description   
%====================================================================================
mqttBroker("127.0.0.1", "1883", "unibo/qak/events").
dispatch( changed, changed(ARG) ).
%====================================================================================
context(ctx_observer, "localhost",  "TCP", "8101").
context(ctx_update, "localhost",  "TCP", "8100").
 qactor( updater, ctx_update, "external").
  qactor( observer, ctx_observer, "it.unibo.observer.Observer").
 static(observer).
