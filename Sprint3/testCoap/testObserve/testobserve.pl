%====================================================================================
% testobserve description   
%====================================================================================
dispatch( changed, changed(ARG) ).
%====================================================================================
context(ctx_update, "localhost",  "TCP", "8100").
context(ctx_observer, "localhost",  "TCP", "8101").
 qactor( updater, ctx_update, "external").
  qactor( observer, ctx_observer, "it.unibo.observer.Observer").
 static(observer).
