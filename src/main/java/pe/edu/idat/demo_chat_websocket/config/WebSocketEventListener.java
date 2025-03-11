package pe.edu.idat.demo_chat_websocket.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pe.edu.idat.demo_chat_websocket.chat.model.Mensaje;
import pe.edu.idat.demo_chat_websocket.chat.model.TipoMensaje;

@Component
public class WebSocketEventListener {
    private final SimpMessageSendingOperations sendingOperations;

    public WebSocketEventListener(SimpMessageSendingOperations sendingOperations) {
        this.sendingOperations = sendingOperations;
    }
    @EventListener
    public void socketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String usuario = (String)headerAccessor.getSessionAttributes().get("username");

        if(usuario != null){
            Mensaje mensaje = new Mensaje();
            mensaje.setTipo(TipoMensaje.DEJAR);
            mensaje.setEnvio(usuario);
            sendingOperations.convertAndSend("/topic/public",mensaje);
        }
    }
}
