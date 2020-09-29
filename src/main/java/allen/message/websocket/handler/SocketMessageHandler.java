package allen.message.websocket.handler;

import allen.message.websocket.SessionManager;
import allen.message.websocket.entity.Message;
import allen.message.websocket.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

import static allen.message.websocket.constants.LongPollConstants.*;

/**
 * 消息处理器
 */
@Slf4j
public class SocketMessageHandler extends TextWebSocketHandler {

    public SocketMessageHandler() {
        subscribeMessage();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (!ObjectUtils.isEmpty(session.getPrincipal().getName())) {
            SessionManager.add(session.getPrincipal().getName(), session);
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession socketSession, TextMessage msg) throws IOException {
        log.info(">>>>>> handleTextMessage start");

        // 1、消息内容解析
        String payload = msg.getPayload();
        Message message = Message.convertToMessage(payload);

        // 2、校验参数是否合法
        verifyRequestParams(socketSession, message);

        // 3、消息处理（模式太多的话，为了方便扩展，可以改成工厂模式 + 策略模式实现）
        switch (message.getModel()) {
            case MODEL_ONE_TO_ONE: {
                handleToOneMessage(message);
                break;
            }
            case MODEL_ONE_TO_GROUP: {
                handleToGroupMessage(message);
                break;
            }
            case MODEL_ONE_TO_ALL: {
                handleToAllMessage(payload);
                break;
            }
            default: {
                log.warn("Not match model...");
                break;
            }
        }
        log.info("<<<<<< handleTextMessage end");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionManager.remove(session.getPrincipal().getName());
        super.afterConnectionClosed(session, status);
    }

    private void verifyRequestParams(WebSocketSession socketSession, Message message) throws IOException {
        if (ObjectUtils.isEmpty(message)) {
            socketSession.sendMessage(new TextMessage("Message send err, request params is null"));
        }

        if (ObjectUtils.isEmpty(message.getModel())) {
            socketSession.sendMessage(new TextMessage("Message send err, message model is null"));
        }

        if (ObjectUtils.isEmpty(message.getGroupId())) {
            socketSession.sendMessage(new TextMessage("Message send err, message groupId is null"));
        }

        if (ObjectUtils.isEmpty(message.getSenderUid())) {
            socketSession.sendMessage(new TextMessage("Message send err, message senderUid is null"));
        }
    }

    private void handleToOneMessage(Message message) throws IOException {
        String groupIdAndUid = message.getGroupId() + message.getReceiverUid();
        WebSocketSession session = SessionManager.get(groupIdAndUid);
        if (!ObjectUtils.isEmpty(session)) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(Message.toJSONString(message)));
            }
        } else {
            // todo 当前服务节点不存在对应用户的session，通知其他节点处理
            // redisClient.publish(CHANNEL_ONE_TO_ONE, Message.toJSONString(message));
        }
    }

    private void handleToGroupMessage(Message message) throws IOException {
        String groupIdAndUid = message.getGroupId() + message.getSenderUid();
        message.setServerPushId(IpUtils.getLocalIpAndAddress());
        List<WebSocketSession> groupOtherSessions = SessionManager.getGroupOtherSessions(message.getGroupId(), groupIdAndUid);
        for (WebSocketSession session : groupOtherSessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(Message.toJSONString(message)));
            }
        }
        // todo 通知其他服务节点处理当前分组的消息
        // Long publish = redisClient.publish(CHANNEL_ONE_TO_GROUP, Message.toJSONString(message));
        WebSocketSession session = SessionManager.get(groupIdAndUid);
        if (session.isOpen()) {
            session.sendMessage(new TextMessage("SUCCESS"));
        }
    }

    private void handleToAllMessage(String payload) throws IOException {
        for (WebSocketSession session : SessionManager.getAllSessions()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(payload));
            }
        }
        // todo 通知其他服务节点处理消息
        // redisClient.publish(CHANNEL_ONE_TO_ALL, payload);
    }

    private void subscribeMessage() {
        // todo 消费其他服务节点的通知，可以使用redis或者消息队列的发布订阅功能
    }
}
