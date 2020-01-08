package cn.arry.netty.customize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @author 云顶之弈江流儿
 * @version 2020/1/8
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength; // 帧长度
    private final int maxFrameSize; // 最大帧长度

    public FixedLengthFrameDecoder(int frameLength) {
        this.frameLength = frameLength;
        this.maxFrameSize = 256;
    }

    public FixedLengthFrameDecoder(int frameLength, int maxFrameSize) {
        this.frameLength = frameLength;
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 过长帧异常
        if (in.readableBytes() >= maxFrameSize) {
            in.clear();
            throw new TooLongFrameException();
        }
        // 帧分割
        while (in.readableBytes() >= frameLength)
            out.add(in.readBytes(frameLength));
    }
}
