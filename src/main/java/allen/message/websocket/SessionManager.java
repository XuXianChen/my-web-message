package allen.message.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接管理器
 * @author Created by xuxiancheng on 2020/9/22
 */
public class SessionManager {

    private final static Logger LOG = LoggerFactory.getLogger(SessionManager.class);

    private final static ConcurrentHashMap<String, WebSocketSession> sessionManager = new ConcurrentHashMap<>();

    public static void add(String groupIdAndUid, WebSocketSession socketSession) {
        LOG.info(">>>>> SocketManager add session, groupIdAndUid:{}", groupIdAndUid);
        sessionManager.put(groupIdAndUid, socketSession);
    }

    public static void remove(String groupIdAndUid) {
        LOG.info("<<<<<< Remove session from SocketManager, sessionKey:{}", groupIdAndUid);
        sessionManager.remove(groupIdAndUid);
    }

    public static WebSocketSession get(String groupIdAndUid) {
        LOG.info("<<<<<< SocketManager get session, groupIdAndUid:{}", groupIdAndUid);
        return sessionManager.get(groupIdAndUid);
    }

    public static List<WebSocketSession> getByGroupId(String groupId) {
        LOG.info(">>>>>> SocketManager getByGroupId start, groupId:{}", groupId);
        List<WebSocketSession> result = new ArrayList<>();
        for(Map.Entry<String, WebSocketSession> entry : sessionManager.entrySet()) {
            String key = entry.getKey();
            if (!ObjectUtils.isEmpty(key) && key.startsWith(groupId)) {
                WebSocketSession value = entry.getValue();
                result.add(value);
            }
        }
        LOG.info("<<<<<< SocketManager getByGroupId, result:{}", result);
        return result;
    }

    public static List<WebSocketSession> getGroupOtherSessions(String groupId, String groupIdAndUid) {
        LOG.info(">>>>>> SocketManager getGroupOtherSessions start, groupId:{}, groupIdAndUid:{}", groupId, groupIdAndUid);
        List<WebSocketSession> result = new ArrayList<>();
        for(Map.Entry<String, WebSocketSession> entry : sessionManager.entrySet()) {
            String key = entry.getKey();
            if (!ObjectUtils.isEmpty(key) && key.startsWith(groupId) && !key.equals(groupIdAndUid)) {
                WebSocketSession value = entry.getValue();
                result.add(value);
            }
        }
        LOG.info("<<<<<< SocketManager getGroupOtherSessions, result:{}", result);
        return result;
    }

    public static Collection<WebSocketSession> getAllSessions() {
        LOG.info(">>>>> SocketManager getAllSessions start");
        return sessionManager.values();
    }
}
