%====================================================================================
% io_devices description   
%====================================================================================
mqttBroker("192.168.137.1", "1883", "unibo/qak/events").
event( sonardata, distance(D) ).
event( stopActions, stopActions(REASON) ).
event( resumeActions, resumeActions(REASON) ).
event( containerhere, containerhere(INFO) ).
dispatch( ledon, ledon(M) ).
dispatch( ledoff, ledoff(M) ).
%====================================================================================
context(ctx_raspdevice, "localhost",  "TCP", "8128").
 qactor( mind, ctx_raspdevice, "it.unibo.mind.Mind").
 static(mind).
  qactor( sonardevice, ctx_raspdevice, "it.unibo.sonardevice.Sonardevice").
 static(sonardevice).
  qactor( leddevice, ctx_raspdevice, "it.unibo.leddevice.Leddevice").
 static(leddevice).
