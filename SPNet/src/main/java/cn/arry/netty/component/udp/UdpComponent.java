package cn.arry.netty.component.udp;

import cn.arry.netty.component.IComponent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * udp component
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
@Getter
public class UdpComponent implements IComponent {
    /**
     * channel
     */
    private Channel channel;

    /**
     * group
     */
    private final EventLoopGroup group;

    public UdpComponent(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        // 引导该NioDatagramChannel
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                // 设置套接字选项SO_BROADCAST
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // your handler
                    }
                });

        channel = bootstrap.bind(address).syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }
}
