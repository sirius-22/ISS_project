package unibo.disi.cargoservicestatusgui.coap;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unibo.basicomm23.utils.CommUtils;
import unibo.disi.cargoservicestatusgui.ws.WebSocketHandler;

import java.util.ArrayList;
import java.util.List;


@Component
public class CoapListener {
	// nome che viene risolto da dentro il container
//	private static final String COAP_ENDPOINT = "coap://arch3:8000/ctx_cargoservice/hold";
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:8000/ctx_cargoservice/cargoservice";

    private CoapClient client;
    private CoapObserveRelation observeRelation;

    @Autowired
    private WebSocketHandler wsHandler;

    @Autowired
    public void init() {
        client = new CoapClient(COAP_ENDPOINT);
        observeRelation = client.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                CommUtils.outblue("CoAP payload: " + content);
                
                // Todo: gestisci il primo messaggio spurio 'noupdate' di CoAP
                
                try {
                    JSONObject payload = parseHoldState(content);
                    if (payload != null) {
                        wsHandler.sendToAll(payload.toString());
                    } else {
                    	CommUtils.outred("Evento CoAP non valido: " + content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                System.err.println("Errore nell'osservazione CoAP.");
            }
        });
        System.out.println("Iniziata osservazione CoAP su: " + COAP_ENDPOINT);
    }
    
    
    public static JSONObject parseHoldState(String message) {
        String jsonString = null;
		JSONObject payload = new JSONObject();
		
        // tolgo eventuali apici
		if (message.startsWith("'") && message.endsWith("'")) {
			jsonString = message.substring(1, message.length()-1);
		} 
		else if(message.startsWith("{")) {
			jsonString = message;
		}
		else {
			// TODO: messaggio spurio di coap, c'è da capire come gestirlo
			jsonString = "{"+message+"}";
		}
		
		// costruisco un oggetto con slot e peso
		System.out.println(jsonString);
		JSONObject original = new JSONObject(jsonString);
//		int currentLoad = original.getInt("currentLoad");
//		JSONObject slotsObject = original.getJSONObject("slots");
//	
//		List<String> slotStatusList = new ArrayList<>();
//		for (int i = 1; i <= 4; i++) {
//			String slotKey = "slot" + i;
//			String status = slotsObject.getString(slotKey);
//			slotStatusList.add(status.equals("occupied") ? "pieno" : "libero");
//		}
//		
//		payload.put("shipLoad", currentLoad);
//		payload.put("slots", slotStatusList);
            
		return original;
    }
}
