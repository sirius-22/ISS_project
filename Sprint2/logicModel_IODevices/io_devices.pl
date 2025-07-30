%====================================================================================
% io_devices description   
%====================================================================================
mqttBroker("192.168.1.214", "1883", "logkb").
event( sonardata, distance(D) ).
event( unibologprolog, unibologprolog(SOURCE,CATEG,CONTENT) ).
dispatch( doblink, doblink(X) ).
%====================================================================================
context(ctx_raspdevice, "localhost",  "TCP", "8128").
 qactor( mind, ctx_raspdevice, "it.unibo.mind.Mind").
 static(mind).
  qactor( sonardevice, ctx_raspdevice, "it.unibo.sonardevice.Sonardevice").
 static(sonardevice).
  qactor( sonarsimul, ctx_raspdevice, "it.unibo.sonarsimul.Sonarsimul").
 static(sonarsimul).
