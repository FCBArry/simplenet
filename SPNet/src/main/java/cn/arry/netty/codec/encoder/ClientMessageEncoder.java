package cn.arry.netty.codec.encoder;

import cn.arry.netty.packet.C2SPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ClientMessageEncoder extends MessageToMessageEncoder<C2SPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, C2SPacket pkg, List<Object> out) throws Exception {
        encode0(ctx, pkg, out);
    }

    private void encode0(ChannelHandlerContext ctx, C2SPacket message, List<Object> out) {
        try {
            int dataLength = message.getLength();
            if (dataLength < 0 || dataLength >= Integer.MAX_VALUE) {
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
