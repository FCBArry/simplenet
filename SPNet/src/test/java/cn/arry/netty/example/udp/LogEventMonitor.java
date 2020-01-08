package cn.arry.netty.example.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * LogEventMonitor
 */
public class LogEventMonitor {
    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        // 引导该NioDatagramChannel
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                // 设置套接字选项SO_BROADCAST
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 将LogEventDecoder和LogEventHandler添加到ChannelPipeline中
                        pipeline.addLast(new LogEventDecoder());
                        pipeline.addLast(new LogEventHandler());
                    }
                })
                .localAddress(address);
    }

    public Channel bind() {
        // 绑定Channel，注意，DatagramChannel是无连接的
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }
}
