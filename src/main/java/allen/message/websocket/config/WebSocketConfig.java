package allen.message.websocket.config;

import allen.message.websocket.handler.PrincipalHandshakeHandler;
import allen.message.websocket.handler.SocketMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static allen.message.websocket.constants.LongPollConstants.WS_MSG_COMMON_URI;

/**
 * webSocket统一配置类
 * @author xuxiancheng
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        SocketMessageHandler messageHandler = new SocketMessageHandler();
        PrincipalHandshakeHandler handshakeHandler = new PrincipalHandshakeHandler();
        registry.addHandler(messageHandler, WS_MSG_COMMON_URI).setHandshakeHandler(handshakeHandler).setAllowedOrigins("*");
    }
}
