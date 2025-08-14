package test.java;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.junit.Test;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.*;

public class CoapUpdateTest {

	private static Interaction conn;
	private String content;

	@BeforeClass
	public static void setup() {
		conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "8000");
	}

	@Test
	public void testUpdateResourceCoap() throws Exception {
		
		CoapClient client = new CoapClient("coap://localhost:8000/ctx_cargoservice/cargoservice");
		
		CountDownLatch latch = new CountDownLatch(1);
		
		java.util.logging.Logger.getLogger("org.eclipse.californium.*").setLevel(java.util.logging.Level.SEVERE);


		CoapObserveRelation relation = client.observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse resp) {
				System.out.println("response: "+resp);
				String c = resp.getResponseText();
				System.out.println("content: "+c);
	            if (c != null && !c.isBlank() && !"nonews".equalsIgnoreCase(c)) {
	                content=c;
	                latch.countDown();
	            }
			}

			@Override
			public void onError() {
				latch.countDown();
			}
		});

		String req = CommUtils.buildRequest("mock", "loadrequest", "loadrequest(1)", "cargoservice").toString();

		String response = conn.request(req);
		System.out.println("richiesta inviata: "+response);

		if (!response.contains("loadaccepted"))
			fail("unexpected rejection");
		
		IApplMessage ev = CommUtils.buildEvent(
	            "test",              // Nome mittente
	            "containerhere",             // Nome evento
	            "containerhere(ok)"          // Contenuto
	        );
		
		conn.forward(ev);

		// Attendo la notifica CoAP con timeout per sicurezza
		boolean updated = latch.await(600, TimeUnit.SECONDS);
		relation.proactiveCancel();

		assertTrue("Nessun update CoAP ricevuto entro il timeout", updated);
		assertNotNull("Ricevuto update nullo", content);
		System.out.println("Update ricevuto: " + content);
	}
}