package allen.message.websocket;

import allen.message.websocket.constants.LongPollConstants;
import allen.message.websocket.entity.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class MyWebSocketClient  extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Connection to server success, connection is open");
    }

    @Override
    public void onMessage(String s) {
        log.info("Received message:{} from server", s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Connection is closed");
    }

    @Override
    public void onError(Exception e) {
        log.info("onError start, message: {}", e.getMessage());
    }

    public static void main(String[] args) {
        final Message message = new Message();
        message.setModel(LongPollConstants.MODEL_ONE_TO_GROUP);
        message.setGroupId("group_1");
        message.setSendTimeMillis(System.currentTimeMillis());
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                final URI uri = new URI("ws://localhost:8080/v1/ws/message?groupId=group_1&uid=uid_1");
                MyWebSocketClient client1 = new MyWebSocketClient(uri);
                client1.connect();
                message.setSenderUid("uid_1");
                message.setContent("你好，我是用户1");
                client1.send(Message.toJSONString(message));
            }
        }).start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                final URI uri = new URI("ws://localhost:8081/v1/ws/message?groupId=group_1&uid=uid_2");
                MyWebSocketClient client2 = new MyWebSocketClient(uri);
                client2.connect();
                message.setSenderUid("uid_2");
                message.setContent("你好，我是用户2");
                client2.send(Message.toJSONString(message));
            }
        }).start();
    }
}
