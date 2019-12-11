package cn.arry.netty.codec.spliter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * c2s packet数据分割校验
 * <p>
 * https://www.jianshu.com/p/a0a51fd79f62
 * https://blog.csdn.net/zougen/article/details/79037675
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
public class ClientMessageSpliter extends LengthFieldBasedFrameDecoder {
    private static final int MAX_FRAME_LENGTH = Integer.MAX_VALUE;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int LENGTH_ADJUSTMENT = -4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    public ClientMessageSpliter() {
        super(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
