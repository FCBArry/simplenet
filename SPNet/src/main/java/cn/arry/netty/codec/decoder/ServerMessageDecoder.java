package cn.arry.netty.codec.decoder;

import cn.arry.netty.packet.S2SPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ServerMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf message, List<Object> out) throws Exception {
        Object decoded = decode(ctx, message);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    private Object decode(ChannelHandlerContext ctx, ByteBuf in) {
//        if (in.readableBytes() < ServerMessage.HDR_SIZE)
//        {
//            return null;
//        }
//
//        ByteBuf out = in.slice();
//        int packetLength = out.getInt(0);
//        if (packetLength <= 0 || packetLength >= Integer.MAX_VALUE)
//        {
//            in.clear();
//            Log.error("ServerMessageDecoder->decode, message invalid length:{} drop this message", packetLength);
//            return null;
//        }
//
//        if (out.readableBytes() < packetLength)
//        {
//            Log.debug("ServerMessageDecoder->decode, readableBytes:{} length:{} readIndex:{} writerIndex:{}",
//                    out.readableBytes(), packetLength, out.readerIndex(), out.writerIndex());
//            return null;
//        }

        ByteBuf out = in.slice();
        S2SPacket message = new S2SPacket();
        S2SPacket packet = message.read(out);
        in.readerIndex(in.readerIndex() + out.readerIndex());

        if (packet == null) {
            // warning log
        }

        return packet;
    }
}
