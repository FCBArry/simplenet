package cn.arry.netty.codec.encoder;

import cn.arry.Log;
import cn.arry.netty.packet.S2SPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * s2s编码
 */
public class ServerMessageEncoder extends MessageToMessageEncoder<S2SPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, S2SPacket pkg, List<Object> out) throws Exception {
        encode0(ctx, pkg, out);
    }

    private void encode0(ChannelHandlerContext ctx, S2SPacket message, List<Object> out) {
        try {
            int dataLength = message.getLength();
            if (dataLength >= Integer.MAX_VALUE || dataLength < 0) {
                Log.error("ServerMessageEncoder->encode, the data is too long, destUserID:{} code:{} length:{}",
                        message.getDestUserID(), message.getCode(), dataLength);
                return;
            }

            ByteBuf buffer = ctx.alloc().buffer(dataLength);
            message.write(buffer);
            out.add(buffer);
        } catch (Exception e) {
            Log.error("ServerMessageEncoder->encode error", e);
            ctx.channel().close();
        }
    }
}
