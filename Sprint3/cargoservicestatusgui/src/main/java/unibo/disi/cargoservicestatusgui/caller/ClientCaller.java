//package unibo.disi.cargoservicestatusgui.caller;
//
//import org.springframework.stereotype.Component;
//import unibo.basicomm23.interfaces.IApplMessage;
//import unibo.basicomm23.interfaces.Interaction;
//import unibo.basicomm23.msg.ApplMessage;
//import unibo.basicomm23.msg.ApplMessageType;
//import unibo.basicomm23.msg.ProtocolType;
//import unibo.basicomm23.utils.CommUtils;
//import unibo.basicomm23.utils.ConnectionFactory;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Component
//public class ClientCaller {
//
//    private Interaction qakConnection;
//    private final String gatewayActorName = "gui_api_gateway";
//    private final String guiContextHost = "localhost"; // L'host dove gira il contesto GUI
//    private final int guiContextPort = 8001;          // La porta del contesto GUI
//    
//    private final WebSocketHandler wsHandler;
//    
//    public ClientCaller(WebSocketHandler wsHandler) {
//        this.wsHandler = wsHandler;
//    }
//
//    @Autowired //  INIEZIONE DELLA DIPENDENZA: Spring crea per noi un oggetto 'qakConnection'
//    public void setup() {
//        try {
//            CommUtils.outblue("ClientCaller | Connecting to QAK context...");
//            qakConnection = ConnectionFactory.createClientSupport23(
//                    ProtocolType.tcp, guiContextHost, String.valueOf(guiContextPort));
//            CommUtils.outgreen("ClientCaller | Connected to QAK context at " + guiContextHost + ":" + guiContextPort);
//        } catch (Exception e) {
//            CommUtils.outred("ClientCaller | Connection to QAK context FAILED: " + e.getMessage());
//        }
//    }
//
//    public void sendLoadRequest(int pid, String sessionId) {
//        if (qakConnection == null) {
//            CommUtils.outred("ClientCaller | Cannot send request, no connection to QAK context.");
//            return;
//        }
//        try {
//            // Usiamo l'ID della sessione WebSocket come ID della richiesta
//            // per permettere la correlazione della risposta
//            String payload = "loadrequest(" + pid + ")";
//            IApplMessage request = new ApplMessage(
//                sessionId, ApplMessageType.request.toString(), "websocket_client", gatewayActorName, payload, "1"
//            );
//            
//            CommUtils.outblue("ClientCaller | Sending request to QAK: " + request);
//            qakConnection.forward(request);
//
//        } catch (Exception e) {
//            CommUtils.outred("ClientCaller | Error sending request: " + e.getMessage());
//        }
//    }
//    
//    private void listenForReplies() {
//        while (true) {
//            try {
//                IApplMessage reply = qakConnection.receiveMsg();
//                if (reply != null) {
//                    CommUtils.outblue("ClientCaller | Received from QAK: " + reply);
//
//                    // inoltra al frontend
//                    wsHandler.reply(reply.msgId(), reply.toString());
//                }
//            } catch (Exception e) {
//                CommUtils.outred("ClientCaller | Error while listening: " + e.getMessage());
//                break;
//            }
//        }
//    }
//}