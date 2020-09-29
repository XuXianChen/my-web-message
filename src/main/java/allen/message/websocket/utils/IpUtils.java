package allen.message.websocket.utils;

import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Set;

@Slf4j
public class IpUtils {
    public static String getLocalIpAndAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostAddress = localHost.getHostAddress();

            // 获取所有注册在jvm中的服务器
            MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
            String port = objectNames.iterator().next().getKeyProperty("port");
            log.info("<<<<<< getLocalIpAndAddress:{}" , (hostAddress + ":" + port));
            return hostAddress + ":" + port;
        } catch (Exception e) {
            log.info("err msg:" + e.getMessage());
            return "";
        }
    }
}
