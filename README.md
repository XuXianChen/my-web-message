当前项目使用spring-boot-starter-websocket搭建的一个实时消息系统demo，功能比较简单

#### Jquery的客户端demo
```javascript
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script>
            var websocket=null;
            $(function(){
                connectWebSocket();
            })           
            //建立WebSocket连接
            function connectWebSocket(){
                console.log("开始...");
                //建立webSocket连接
                websocket = new WebSocket("ws://localhost:8082/v1/ws/message?groupId=group_1&uid=uid_1");              
                //打开webSokcet连接时，回调该函数
                websocket.onopen = function () {      
                        console.log("onpen,time: " + new Date().toLocaleTimeString());  
                }                
                //关闭webSocket连接时，回调该函数
                websocket.onclose = function () {
                //关闭连接    
                        console.log("onclose,time: " + new Date().toLocaleTimeString());
                }
                //接收信息
                websocket.onmessage = function (msg) {
                        console.log("onmessage:" + msg.data);
                }
            }
            //发送消息
            function send(content){
                var postValue={};
                postValue.senderUid = "uid_1";
                postValue.groupId = "group_1";  
                postValue.content = content;  
                postValue.id = "11111";  
                postValue.model = "ONE_TO_GROUP";
                console.log("分组1，用户1，开始发送消息：" + JSON.stringify(postValue));       
                websocket.send(JSON.stringify(postValue));
            }
            //关闭连接
            function closeWebSocket(){
                if(websocket != null) {
                    websocket.close();
                }
            }
        </script>
```

#### 注意事项：   
- websocket协议建立连接（使用http升级协议）的时候，如果使用nginx，需要进行额外的配置
- websocket连接一段时间没有发送消息，会自动断开连接，所以特定场景一般需要客户端定时的发送心跳来维持连接
- spring-boot-starter-websocket测试过程中，消息太大，会拒绝接受消息
- 多服务节点的情况下，为了实现session全局管理，可以使用redis的发布订阅功能，也可以使用消息队列来实现，并且消息转发过程中可以通过具体服务节点的ip和端口进行区分，避免消费自己的消息
- 消息丢失、消息持久化情况该demo中没有实现，需要自行实现

