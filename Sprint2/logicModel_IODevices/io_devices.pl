%====================================================================================
% io_devices description   
%====================================================================================
mqttBroker("192.168.1.214", "1883", "logkb").
event( sonardata, distance(D) ).
event( unibologprolog, unibologprolog(SOURCE,CATEG,CONTENT) ).
event( stopActions, stopActions(M) ).
event( resumeActions, resumeActions(M) ).
event( containerhere, containerhere(M) ).
event( ledon, ledon(M) ).
event( ledoff, ledoff(M) ).
dispatch( doblink, doblink(X) ).
%====================================================================================
context(ctx_raspdevice, "localhost",  "TCP", "8128").
 qactor( mind, ctx_raspdevice, "it.unibo.mind.Mind").
 static(mind).
  qactor( sonardevice, ctx_raspdevice, "it.unibo.sonardevice.Sonardevice").
 static(sonardevice).
  qactor( leddevice_mock, ctx_raspdevice, "it.unibo.leddevice_mock.Leddevice_mock").
 static(leddevice_mock).
  qactor( sonarsimul, ctx_raspdevice, "it.unibo.sonarsimul.Sonarsimul").
 static(sonarsimul).
