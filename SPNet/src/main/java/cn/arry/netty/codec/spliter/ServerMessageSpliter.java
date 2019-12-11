package cn.arry.netty.codec.spliter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * s2s packet数据分割校验
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
public class ServerMessageSpliter extends LengthFieldBasedFrameDecoder {
    private static final int MAX_FRAME_LENGTH = Integer.MAX_VALUE;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int LENGTH_ADJUSTMENT = -4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    public ServerMessageSpliter() {
        super(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
