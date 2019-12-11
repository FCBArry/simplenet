package cn.arry.netty.initializer;

import cn.arry.netty.codec.MessageCodecFactory;
import cn.arry.netty.codec.spliter.ClientMessageSpliter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * client childHandler ChannelInitializer
 */
@Sharable
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private ChannelHandler handler;

    public ClientChannelInitializer(ChannelHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Channel handler is null");
        }

        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ClientMessageSpliter());
        pipeline.addLast("decoder", MessageCodecFactory.getCDecoder());
        pipeline.addLast("encoder", MessageCodecFactory.getCEncoder());
        pipeline.addLast("handler", handler);
    }
}
