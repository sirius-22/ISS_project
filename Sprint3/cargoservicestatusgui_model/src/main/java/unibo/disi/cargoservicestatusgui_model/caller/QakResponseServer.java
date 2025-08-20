package unibo.disi.cargoservicestatusgui_model.caller;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IApplMsgHandler;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.enablers.ServerFactory;
import unibo.basicomm23.utils.CommUtils;
import unibo.disi.cargoservicestatusgui_model.ws.WebSocketHandler;

@Component
public class QakResponseServer {

    private final int serverPort = 8002;

    @PostConstruct
    public void startServer() {
        CommUtils.outblue("QakResponseServer | Starting TCP server on port " + serverPort);
        
        IApplMsgHandler responseHandler = new IApplMsgHandler() {
            @Override
            public void elaborate(IApplMessage msg, Interaction conn) {
                CommUtils.outmagenta("QakResponseServer | Received response from QAK: " + msg);
                
                if (msg.msgId().equals("load_response")) {
                    try {
                        String content = msg.msgContent();
                        
                        String innerContent = content.substring(content.indexOf("(") + 1, content.length() - 1);

                        int firstQuoteIndex = innerContent.indexOf("'");
                        int secondQuoteIndex = innerContent.indexOf("'", firstQuoteIndex + 1);
                        String sessionId = innerContent.substring(firstQuoteIndex + 1, secondQuoteIndex);
                        
                        int jsonStartIndex = innerContent.indexOf("'", secondQuoteIndex + 1);
                        int jsonEndIndex = innerContent.lastIndexOf("'");
                        String jsonResponse = innerContent.substring(jsonStartIndex + 1, jsonEndIndex);
                        
                        jsonResponse = CommUtils.restoreFromConvertToSend(jsonResponse);

                        WebSocketHandler.getInstance().reply(sessionId, jsonResponse);

                    } catch (Exception e) {
                        CommUtils.outred("QakResponseServer | ERROR parsing response message '" + msg.msgContent() + "': " + e.getMessage());
                    }
                }
            }

            @Override
            public String getName() {
                return "QakResponseHandler";
            }
        };

        ServerFactory server = new ServerFactory("QakResponseServer", serverPort, ProtocolType.tcp, responseHandler);
        server.start();
    }
}