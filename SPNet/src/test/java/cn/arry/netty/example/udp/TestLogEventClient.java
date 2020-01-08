package cn.arry.netty.example.udp;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * log event udp client
 *
 * @author 云顶之弈江流儿
 * @version 2020/1/8
 */
public class TestLogEventClient {
    public static void main(String[] args) throws Exception {
        // 创建并启动一个新的LogEventBroadcaster实例
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255", 9523),
                new File("../resources/file/logeventtest.txt"));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
