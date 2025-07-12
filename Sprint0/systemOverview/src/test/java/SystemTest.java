package test.java;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.BeforeClass;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;


public class SystemTest {
	
	private static Interaction conn;
	
	@BeforeClass
	public void setup() throws Exception {
		conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "11800");
		//porta di ctx_cargoservice
		/*
		 * inizializzazione prodotti...
		 */
	}
	
	@Test
    public void testLoadRequestInvalidPID() throws Exception {
        //richiesta con PID invalido
        String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(-1)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata rifiutata
        assertTrue("Test PID invalido", 
                 response.contains("loadrejected"));
    }

	@Test
	public void testLoadRequestAccepted() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(1)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata accettata
        assertTrue("Test richiesta accettata", 
                response.contains("loadaccepted"));
    }
	
	@Test
	public void testLoadRequestTooHeavy() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(2)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata rifiutata per il peso
        assertTrue("Test troppo pesante", 
                response.contains("loadrejected") && 
                response.contains("too_heavy"));
    }

	@Test
	public void testLoadRequestNoFreeSlots() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(3)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata rifiutata per mancanza di slot liberi
        assertTrue("Test slot pieni", 
                response.contains("loadrejected") && 
                response.contains("no_slots"));
    }
		
	
}
