%====================================================================================
% io_devices description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/qak/events").
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
  qactor( sonarsimul, ctx_raspdevice, "it.unibo.sonarsimul.Sonarsimul").
 static(sonarsimul).
