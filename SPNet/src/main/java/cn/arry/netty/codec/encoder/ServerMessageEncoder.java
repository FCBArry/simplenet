package cn.arry.netty.codec.encoder;

import cn.arry.netty.packet.S2SPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ServerMessageEncoder extends MessageToMessageEncoder<S2SPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, S2SPacket pkg, List<Object> out) throws Exception {
        encode0(ctx, pkg, out);
    }

    private void encode0(ChannelHandlerContext ctx, S2SPacket message, List<Object> out) {
        try {
            int dataLength = message.getLength();
            if (dataLength >= Integer.MAX_VALUE || dataLength < 0) {
                // warning log
                return;
            }

            ByteBuf buffer = ctx.alloc().buffer(dataLength);
            message.write(buffer);
            out.add(buffer);
        } catch (Exception e) {
            // warning log
            ctx.channel().close();
        }
    }
}
