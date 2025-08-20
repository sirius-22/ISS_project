package unibo.disi.cargoservicestatusgui_model.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import unibo.disi.cargoservicestatusgui_model.caller.*;

import unibo.basicomm23.utils.CommUtils;
import unibo.disi.cargoservicestatusgui_model.caller.ClientCaller;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONObject;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ClientCaller ClientCaller;
    
    // Singleton instance
    private static WebSocketHandler singleton;

    public WebSocketHandler(ClientCaller ClientCaller) {
        this.ClientCaller = ClientCaller;
        singleton = this; // Set singleton instance
    }

    public static WebSocketHandler getInstance() {
        return singleton;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        CommUtils.outgreen("WebSocketHandler | New connection established: " + session.getId() + ". Total sessions: " + sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        CommUtils.outblue("WebSocketHandler | Received message from " + session.getId() + ": " + payload);

        // Parsa il messaggio JSON in arrivo dal frontend
        JSONObject json = new JSONObject(payload);
        String type = json.getString("type");

        if ("loadrequest".equals(type)) {
            int pid = json.getInt("pid");
            // Inoltra la richiesta al sistema Qak usando l'ID di sessione come ID richiesta
            ClientCaller.sendLoadRequest(pid, session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        CommUtils.outred("WebSocketHandler | Connection closed: " + session.getId() + ". Total sessions: " + sessions.size());
    }

    /**
     * Invia un messaggio a tutti i client connessi.
     * Usato da GuiStateObserver per inviare lo stato della stiva.
     * @param message Il messaggio (JSON dello stato della stiva) da inviare.
     */
    public void sendToAll(String message) {
        TextMessage textMessage = new TextMessage(message);
        CommUtils.outyellow("JSON WS | "+textMessage);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                CommUtils.outred("WebSocketHandler | Error sending to session " + session.getId() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Invia una risposta a una sessione specifica.
     * Usato da GuiRequestHandler.
     * @param sessionId L'ID della sessione a cui rispondere (che abbiamo usato come ID richiesta)
     * @param message La risposta JSON da inviare.
     */
    public void reply(String sessionId, String message) {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions) {
            if (session.getId().equals(sessionId)) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(textMessage);
                        CommUtils.outgreen("WebSocketHandler | Replied to session " + sessionId);
                    }
                } catch (IOException e) {
                    CommUtils.outred("WebSocketHandler | Error replying to session " + sessionId + ": " + e.getMessage());
                }
                return; // Trovato e inviato, esci
            }
        }
        CommUtils.outyellow("WebSocketHandler | Could not find session to reply to: " + sessionId);
    }
}