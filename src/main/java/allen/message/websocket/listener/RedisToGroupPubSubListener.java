package allen.message.websocket.listener;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

/**
 * redis发布-订阅的订阅者，处理消息发送的逻辑，也可以使用消息队列
 * @author xuxiancheng
 */
@Slf4j
public class RedisToGroupPubSubListener extends JedisPubSub {

    @SneakyThrows
    @Override
    public void onMessage(String channel, String msg) {
        //todo
        super.onMessage(channel, msg);
    }

}
