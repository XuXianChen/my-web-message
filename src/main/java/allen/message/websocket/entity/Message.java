package allen.message.websocket.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 统一消息封装
 * @author Created by xuxiancheng on 2020/9/22
 */
@Data
public class Message {
    private String id; // 消息唯一标识
    private String model; // 消息发送模式
    private String senderUid; // 发送者id
    private String receiverUid; // 接收者用户id
    private String groupId; // 分组id（抽象概率，可以理解为直播间id，房间id等）
    private String username; // 用户名
    private String clientId; // 客户端id
    private String content; // 消息内容
    private Long sendTimeMillis; // 消息发送时间戳
    private String serverPushId; // 发布订阅发布者本机ip
    private String evtCode; // 事件的状态码
    private String evtName; // 事件名称
    private String evtType; // 事件类型
    private String evtAttrs; // 事件扩展字段
    private String otherAttrs; // 其他扩展字段

    public static Message convertToMessage(String message) {
        return JSON.parseObject(message, Message.class);
    }

    public static String toJSONString(Message message) {
        return JSON.toJSONString(message);
    }
}
