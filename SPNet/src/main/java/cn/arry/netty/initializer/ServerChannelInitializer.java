package cn.arry.netty.initializer;

import cn.arry.netty.codec.MessageCodecFactory;
import cn.arry.netty.codec.spliter.ServerMessageSpliter;
import cn.arry.netty.handler.CommonIdleStateHandler;
import cn.arry.netty.handler.HeartbeatHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * server childHandler ChannelInitializer
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private ChannelHandler handler;

    public ServerChannelInitializer(ChannelHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Channel handler is null");
        }

        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ServerMessageSpliter());
        pipeline.addLast(new CommonIdleStateHandler());
        pipeline.addLast(new HeartbeatHandler());
        pipeline.addLast("decoder", MessageCodecFactory.getSDecoder());
        pipeline.addLast("encoder", MessageCodecFactory.getSEncoder());
        pipeline.addLast("handler", handler);
    }
}
