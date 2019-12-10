package cn.arry.netty.codec.decoder;

import cn.arry.netty.packet.C2SPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ClientMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    private Object decode(ChannelHandlerContext ctx, ByteBuf in) {
//        if (in.readableBytes() < ClientMessage.HDR_SIZE)
//        {
//            return null;
//        }
//
//        ByteBuf out = in.slice();
//        int packetLength = out.getInt(0);
//        if (packetLength <= 0 || packetLength >= Integer.MAX_VALUE)
//        {
//            in.clear();
//            Log.error("ClientMessageDecoder->decode, message invalid length:{} drop this message", packetLength);
//            return null;
//        }
//
//        if (out.readableBytes() < packetLength)
//        {
//            Log.debug("ClientMessageDecoder->decode, readableBytes:{} length:{} readIndex:{} writerIndex:{}",
//                    out.readableBytes(), packetLength, out.readerIndex(), out.writerIndex());
//            return null;
//        }

        ByteBuf out = in.slice();
//        int checksum = out.getShort(4);
//        int code = out.getShort(6);
//        long userID = out.getLong(8);
        C2SPacket message = new C2SPacket();
        C2SPacket packet = message.read(out);
        in.readerIndex(in.readerIndex() + out.readerIndex());

        if (packet == null) {
            // warning log
        }

        return packet;
    }
}
