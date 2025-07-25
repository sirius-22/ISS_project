%====================================================================================
% test description   
%====================================================================================
event( alarm, alarm(M) ).
event( nooalarm, nooalarm(M) ).
dispatch( aaaa, aaaa(A) ).
%====================================================================================
context(ctx_testing, "localhost",  "TCP", "11800").
 qactor( test1, ctx_testing, "it.unibo.test1.Test1").
 static(test1).
  qactor( test2interrupt, ctx_testing, "it.unibo.test2interrupt.Test2interrupt").
 static(test2interrupt).
