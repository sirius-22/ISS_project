package test.java;

import static org.junit.Assert.*;
import org.junit.BeforeClass;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;


import main.java.hold.*;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ConnectionFactory;
import main.java.domain.*;

public class CoapUpdateTest {
	
	private static Interaction conn;
    private static final String COAP_ENDPOINT = "coap://localhost:8050/webgui/holdstate";

    @BeforeClass
    public static void setup() {
    	conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "localhost", "11800");
    }
    
    @Test
	public void testUpdateResourceCoap() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(1)", 
                "cargoservice").toString();
        
        String response = conn.request(req);
        
        if (!response.contains("loadaccepted"))
        	fail("unexpected rejection")
        	
        
    }
}