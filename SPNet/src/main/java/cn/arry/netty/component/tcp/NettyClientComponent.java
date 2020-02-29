package cn.arry.netty.component.tcp;

import cn.arry.Log;
import cn.arry.netty.component.IComponent;
import cn.arry.netty.packet.C2SPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

/**
 * client component
 * start as a client
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
@Getter
public class NettyClientComponent implements IComponent {
    /**
     * channel
     */
    private Channel channel;

    private static class LazyHolder {
        private static final NettyClientComponent INSTANCE = new NettyClientComponent();
    }

    public static NettyClientComponent getInstance() {
        return NettyClientComponent.LazyHolder.INSTANCE;
    }

    /**
     * connect
     * TODO 调参
     */
    public void connect(String address, int port, ChannelHandler handler) {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(handler);

            channel = bootstrap.connect(address, port).sync().channel();
        } catch (Exception e) {
            Log.error("NettyClientComponent->connect error", e);
        }
    }

    public void send(C2SPacket c2SPacket) {
        channel.writeAndFlush(c2SPacket);
    }

    public void stop() {
        try {
            if (channel != null) {
                channel.closeFuture().sync();
            }
        } catch (Exception e) {
            Log.error("NettyClientComponent->stop error", e);
        }
    }
}
