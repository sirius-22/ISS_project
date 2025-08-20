//package unibo.disi.cargoservicestatusgui.ws;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WSConfig implements WebSocketConfigurer {
//    private final WebSocketHandler handler;
//
//    public WSConfig(WebSocketHandler handler) {
//        this.handler = handler;
//    }
//    
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(handler, "/status-updates")
//                .setAllowedOrigins("*");
//    }
//}