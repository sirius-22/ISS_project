package main.java.caller;

/*
 * ---------------------------------------------
 * WARNING:
 *  	ATTIVARE con gradlew runCaller
 *      configurare eureka-client.properties
 * ---------------------------------------------
 */
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class BasicrobotDiscoverCallerTcp  {
	
	private Interaction connSupport;

    protected IApplMessage turnL = CommUtils.buildDispatch("gui23xyz9526", "cmd", "cmd(l)", "basicrobot");
    protected IApplMessage turnR = CommUtils.buildDispatch("gui23xyz9526", "cmd", "cmd(r)", "basicrobot");
    protected IApplMessage doplan = CommUtils.buildRequest("gui23xyz9526", "doplan", "doplan([w,w],350)", "basicrobot");
    protected IApplMessage move53 = CommUtils.buildRequest("gui23xyz9526", "moverobot", "moverobot(5,3)", "basicrobot");
    protected IApplMessage moveHome = CommUtils.buildRequest("gui23xyz9526", "moverobot", "moverobot(0,0)", "basicrobot");
    protected IApplMessage setHome  = CommUtils.buildDispatch("gui23xyz9526", "setrobotstate", "setpos(0,0,down)", "basicrobot");

    
	protected String[] discoverBasicrobt(String serviceName) {
 		CommUtils.ckeckEureka( );
 		//DISCOVER
    	CommUtils.outyellow(" ---------------------------- discoverService ");
 		String[]  hostPort = CommUtils.discoverService(  serviceName ); 
		CommUtils.outyellow(" ---------------------------- discoverService hostPort=" + hostPort);
 		if( hostPort != null ) {
			CommUtils.outcyan("connectService hostPort:     " + hostPort[0] +":"+hostPort[1]);
			return hostPort;
		}else {
			CommUtils.outred("Discoverable " + serviceName + " not found");
			return null;
		}			

	}
	
	
	protected boolean connectToService(String[]  hostPort) {
		try {			 
			if( hostPort != null ) {
				String host = hostPort[0];
				String port = hostPort[1];
				CommUtils.outcyan("connectService Hostname: " + host);
				CommUtils.outcyan("connectService Port:     " + port);
 				connSupport = //new TcpConnection(host, Integer.parseInt(port));
				 ConnectionFactory.createClientSupport23(ProtocolType.tcp, host, port);
 				return true;
			}else {
				CommUtils.outred("no service available");
			}
		} catch (Exception e) {
			CommUtils.outred("ERROR:" + e.getMessage());
		}
		return false;
	}

	protected void userRobot( String[]  hostPort ) {
		try {
			connectToService(hostPort);
			
			connSupport.forward(setHome);
			
//			connSupport.forward(turnL);
//			CommUtils.delay(500);
//			connSupport.forward(turnR);
//			CommUtils.delay(500);
			
			IApplMessage answer = connSupport.request(move53);
			CommUtils.outblue(answer.toString());
			
			answer = connSupport.request(moveHome);
			CommUtils.outblue(answer.toString());
			
			connSupport.forward(turnL);
			
 		} catch (Exception e) {
			CommUtils.outred("ERROR:" + e.getMessage());
		}
		
	}
  
	
	public void doJob() throws Exception   {
		CommUtils.outcyan("connectService doJob"  );
		String[]  hostPort = discoverBasicrobt("ctxbasicrobot");
		if( hostPort != null ) {
            userRobot(hostPort);
		}

        System.exit(0);
	}
	
//    protected void shutDownTheClient() throws Exception {
//        // Chiudi il client Eureka
// 		CommUtils.delay(1000); 
// 		//deregister();
// 		CommUtils.delay(1000); 
//    	CommUtils.outblue("ServiceUsage shutting down the EurekaClient after 1 sec delay");
//		connSupport.close();
// 	
//    }
	 

	public static void main(String[] args) throws Exception  {
		//TCP call requires knowledge at system level!
 		BasicrobotDiscoverCallerTcp caller = new BasicrobotDiscoverCallerTcp( );
		caller.doJob();
	}

}
