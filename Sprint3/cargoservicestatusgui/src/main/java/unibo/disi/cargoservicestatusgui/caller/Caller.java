package unibo.disi.cargoservicestatusgui.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unibo.disi.cargoservicestatusgui.ws.WSHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

@Component
public class Caller {
	
	@Autowired
    private WSHandler wsHandler;

    private Interaction conn;

    public Caller() {
        try {
        	// usa questo da dentro i container
//        	conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "arch3", "8000");
            conn = ConnectionFactory.createClientSupport23(ProtocolType.tcp, "127.0.0.1", "8000");
        } catch (Exception e) {
            System.err.println("Errore nella connessione TCP iniziale: " + e.getMessage());
        }
    }

    @GetMapping("/caller")
    public String callCargoservice(@RequestParam("pid") String pid) {
//        try {
//        	CommUtils.outblue("send request to cargoservice");
//    	 	IApplMessage getreq = CommUtils.buildRequest ("webgui", "load_product","load_product("+pid+")", "cargoservice");
//            IApplMessage answer = conn.request(getreq);  //raises exception
//            CommUtils.outgreen("response" + answer);
//            return answer.msgContent();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "{\"error\":\"" + e.getMessage() + "\"}";
//        }
    	
    	
    	
    	
    	
    	System.out.println("DEBUG: Received /caller request with pid = " + pid);
        return "{\"status\":\"ok\"}";
    	
    }

}
