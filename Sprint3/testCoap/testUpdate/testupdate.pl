%====================================================================================
% testupdate description   
%====================================================================================
%====================================================================================
context(ctx_update, "localhost",  "TCP", "8100").
context(ctx_observer, "localhost",  "TCP", "8101").
 qactor( observer, ctx_observer, "external").
  qactor( updater, ctx_update, "it.unibo.updater.Updater").
 static(updater).
