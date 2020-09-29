package allen.message.websocket.listener;


import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * redis发布-订阅的订阅者，处理消息发送的逻辑，也可以使用消息队列
 * @author xuxiancheng
 */
public class RedisToAllPubSubListener extends JedisPubSub {

    private final static Logger LOG = LoggerFactory.getLogger(RedisToAllPubSubListener.class);

    @SneakyThrows
    @Override
    public void onMessage(String channel, String msg) {
        // todo
        super.onMessage(channel, msg);
    }

}
