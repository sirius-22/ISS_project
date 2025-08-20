package main.java.caller;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.unibo.kactor.sysUtil;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;
import unibo.basicomm23.utils.ConnectionFactory;
 
public class BasicRobotCallerCoap  {
	private static final Logger logger = LoggerFactory.getLogger("BasicRobotCallerCoap");
 
    protected String name;
    protected Interaction commChannel;
    protected ProtocolType protocol;
    protected String hostAddr;
    protected String entry;
    protected boolean connected = false;
    
    protected CoapObserveRelation relation;
    
    protected IApplMessage turnL = CommUtils.buildDispatch("gui23xyz9526", "cmd", "cmd(l)", "basicrobot");
    protected IApplMessage turnR = CommUtils.buildDispatch("gui23xyz9526", "cmd", "cmd(r)", "basicrobot");
    protected IApplMessage doplan = CommUtils.buildRequest("gui23xyz9526", "doplan", "doplan([w,w],350)", "basicrobot");
    protected IApplMessage move53 = CommUtils.buildRequest("gui23xyz9526", "moverobot", "moverobot(5,3)", "basicrobot");
    protected IApplMessage moveHome = CommUtils.buildRequest("gui23xyz9526", "moverobot", "moverobot(0,0)", "basicrobot");

    protected IApplMessage atHome = CommUtils.buildDispatch("gui23xyz9526", "setrobotstate", "setpos(0,0,down)", "basicrobot");
     
	public BasicRobotCallerCoap(String name, ProtocolType protocol, String hostAddr, String entry) {
        this.name     = name;
        this.protocol = protocol;
        this.hostAddr = hostAddr;
        this. entry   = entry;
 
        CommUtils.outblue(name + " | CREATED "  );
		sysUtil.clearlog("./logs/basicrobot24caller.log");
		logger.info("avviato correttamente.");
//		logger.debug("Questo è un messaggio di debug.");
	}


    public void activate(){
        new Thread(){
            public void run(){
                try {
                    connect();
                    body();
                  } catch (Exception e) {
                    CommUtils.outred("activate ERROR:"+e.getMessage());
                }
            }
        }.start(); 
    }

    protected void connect() throws Exception{ 
       CommUtils.outblue(name + " |  connect "  + connected);
       if( connected ) return;
         	commChannel = ConnectionFactory.createClientSupport23(protocol, hostAddr, entry);
//        	CoapConnection.create(hostAddr, entry);
         	connected   = true;
        	//((Connection)commChannel).trace = true; 
        	//CommUtils.outblue(name + " | exit connect client=" + commChannel);
         	addObservation( );
    }
	 
	protected void body() throws Exception {
		IApplMessage answer ;
		CommUtils.outblue(name + " | setpos atHome "  );
		commChannel.forward( atHome  );
		 

		
		CommUtils.outblue(name + " | sends " + turnL);
		commChannel.forward(turnL);
		CommUtils.delay(500);
		CommUtils.outblue(name + " | sends " + turnR);
		commChannel.forward(turnR);
		CommUtils.delay(500);
//		CommUtils.outblue(name + " | sends "  + doplan );
//		CommUtils.delay(500);	 
//		IApplMessage answer = commChannel.request( doplan  );
//		CommUtils.outblue(name +  " answer= | "  + answer );

		answer = commChannel.request( move53  );
		CommUtils.outblue(name +  " answer= | "  + answer );
		answer = commChannel.request( moveHome  );
		CommUtils.outblue(name +  " answer= | "  + answer ); 
		CommUtils.outblue(name + " | sends " + turnL);
		commChannel.forward(turnL);

		
		CommUtils.delay(5000);
 		relation.proactiveCancel();
 		CommUtils.outblue("Resource observer | ENDS"   );
		CommUtils.outblue(name + "| BYE" );
        System.exit(0);
	}

 	
	protected void addObservation( ) {
    //OSSERVO robotPos
		Interaction conn = ConnectionFactory.createClientSupport23(protocol, hostAddr, "ctxbasicrobot/robotpos");
		CoapClient client = ((CoapConnection)conn).getClient();
	    CommUtils.outblue("callerCoap addObservation client"  );
		relation = client.observe(
				new CoapHandler() {
					@Override public void onLoad(CoapResponse response) {
						String content = response.getResponseText();
						//CommUtils.outgreen("Resource observer | value=" + content );
						CommUtils.outgreen( content );
					}					
					@Override public void onError() {
						CommUtils.outred("OBSERVING FAILED  ");
					}
				});	
		
	}

	 
	public static void main(String[] args) {
		BasicRobotCallerCoap caller = 
			new BasicRobotCallerCoap(
					"callercoap", ProtocolType.coap,"localhost:8020", "ctxbasicrobot/basicrobot"); //basicrobot
		caller.activate();
	}

}
