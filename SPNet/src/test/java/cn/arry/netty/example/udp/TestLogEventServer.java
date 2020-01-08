package cn.arry.netty.example.udp;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * log event udp server
 *
 * @author 云顶之弈江流儿
 * @version 2020/1/8
 */
public class TestLogEventServer {
    public static void main(String[] args) throws Exception {
        // 构造一个新的LogEventMonitor实例
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(9523));
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running");
            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }
}
