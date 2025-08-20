package main.java;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class MqttJavaPublisher {
	private String topic     = "robotevents";	
	private String brokerAddr= "tcp://192.168.1.132:1883"; //"tcp://localhost:1883" ;    //
	private MqttClient client;
	private String alarmevent = "msg(alarm,event,javaclient,none,alarm(D),0)";
	
	public MqttJavaPublisher() {
		
		 
	}
	
	public void doJob() {
		try {
			MqttConnection mqttconn = (MqttConnection) ConnectionFactory.createClientSupport(
					ProtocolType.mqtt,  brokerAddr, "publisher");
			CommUtils.outblue("client CONNECTED " + mqttconn);
			mqttconn.publish( topic, alarmevent.replace("D","61" ));
			CommUtils.delay(2000);
			CommUtils.outblue("client BYE "  );
			System.exit(0);
		} catch (Exception e) {
			CommUtils.outred("ERROR " + e);
		}
	}

    public void doJobBasic() {
        try {
        	client = new MqttClient(brokerAddr, "clientjava", new MemoryPersistence());
    		client.connect();
  			CommUtils.outblue("client CONNECTED");
  			for( int i=1; i<4; i++) {
  				String msg = alarmevent.replace("D",(""+i*10) );
	  			sendMessageMqtt(msg);
	  			CommUtils.delay(4000);
  			}
  			System.exit(0);
        }catch(Exception e){
          CommUtils.outred("ERROR " + e.getMessage() );
        }
      }
    
    protected  void sendMessageMqtt( String m  )
            throws MqttSecurityException, MqttException {
      CommUtils.outblue("client sendMessageMqttd " + m);
	  MqttMessage mqttmsg = new MqttMessage();
	  mqttmsg.setQos(2);
	  mqttmsg.setPayload(m.getBytes());
	  client.publish(topic, mqttmsg);
	  //CommUtils.outblue("client HAS published " + mqttmsg);
	}    
    
    public static void main( String[] args)   {
        new MqttJavaPublisher().doJob();

      }    
    
}
