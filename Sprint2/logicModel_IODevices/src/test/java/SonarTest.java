package test.java;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;

import org.junit.BeforeClass;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnectionBaseInSynch;
import unibo.basicomm23.mqtt.MqttConnectionBaseOut;
import unibo.basicomm23.utils.CommUtils;

public class SonarTest {
	
	private static MqttConnectionBaseInSynch mqttConn;
	private final static String MqttBroker= "tcp://broker.hivemq.com";
	private static MqttConnectionBaseOut mqttOutConn;
	
	@BeforeClass
	public static void setup() throws MqttException {
		mqttConn = new MqttConnectionBaseInSynch(MqttBroker,"test", "unibo/qak/events");
		mqttOutConn = new MqttConnectionBaseOut(MqttBroker, "testerMock", "sonardatatest");
	}
	
	@Test
    public void testContainerArrived() throws Exception {
		IApplMessage dist_containerhere = CommUtils.buildEvent("tester","sonardata", "distance(5)");
        //QakContext.emit(dist_containerhere, false, null);
		mqttOutConn.send(dist_containerhere.toString());
		mqttOutConn.send(dist_containerhere.toString());
		mqttOutConn.send(dist_containerhere.toString());
		String msg = mqttConn.receive();   // bloccante
        assertTrue(msg.contains("containerhere"));
	}
	
	@Test
	public void testFaultAndResume() throws Exception {
		IApplMessage dist_fault = CommUtils.buildEvent("tester","sonardata", "distance(25)");
		IApplMessage dist_resume = CommUtils.buildEvent("tester","sonardata", "distance(5)");
		
        //QakContext.emit(dist_containerhere, false, null);
		mqttOutConn.send(dist_fault.toString());
		mqttOutConn.send(dist_fault.toString());
		mqttOutConn.send(dist_fault.toString());
		String msg = mqttConn.receive();   // bloccante
        assertTrue(msg.contains("stopActions"));
        
        mqttOutConn.send(dist_resume.toString());
		mqttOutConn.send(dist_resume.toString());
		mqttOutConn.send(dist_resume.toString());
		msg = mqttConn.receive();   // bloccante
        assertTrue(msg.contains("resumeActions"));
	}
	
	@Test
	public void noEarlyEvent() throws Exception{
		IApplMessage dist_containerhere = CommUtils.buildEvent("testerContainer","sonardata", "distance(5)");
		mqttOutConn.send(dist_containerhere.toString());
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit(() -> mqttConn.receive());

		try {
		    String msg = future.get(5, TimeUnit.SECONDS); // aspetta max 1s
		    fail("I expected to get no messages but received the following: " + msg);
		} catch (TimeoutException e) {
		    // test passato: non è arrivato niente entro il timeout
		} finally {
		    executor.shutdownNow();
		}
	}
}