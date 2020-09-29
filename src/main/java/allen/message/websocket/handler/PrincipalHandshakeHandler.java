package allen.message.websocket.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * 连接建立后，为每个连接创建一个唯一标识
 */
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletServerHttpRequest.getServletRequest();
            final String groupId = httpRequest.getParameter("groupId");
            final String uid = httpRequest.getParameter("uid");
            if (ObjectUtils.isEmpty(groupId) || ObjectUtils.isEmpty(uid)) {
                return null;
            }
            return new Principal() {
                @Override
                public String getName() {
                    return groupId + uid;
                }
            };
        }
        return null;
    }
}
