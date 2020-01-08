package cn.arry;

import cn.arry.netty.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author 云顶之弈江流儿
 * @version 2020/1/8
 */
public class SPNetTest {
    public static void main(String[] args) {

    }

    @Test
    public void testNetty() {
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(4));

        // 测试入站写入
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 10; i++)
            buf.writeByte(i);
        ByteBuf in = buf.duplicate();
        // 入站写入3个字节，此时Decoder会缓存这些数据，并没有转发这些数据到下一个ChannelHandler
        assertFalse(channel.writeInbound(in.readBytes(3)));

        // 入站写入7个字节，加上之前写入的3个字节，Decoder转发其中前8个字节，分为2组转发给下一个ChannelHandler，剩余2个字节仍被缓存
        assertTrue(channel.writeInbound(in.readBytes(7)));
        assertTrue(channel.finish()); // 向通道发送结束信号

        // 测试入站读取
        // 由上面的写入过程可以估计，前2次都可以读取到值，第3次读取为空值
        ByteBuf read = channel.readInbound();
        assertEquals(read, buf.readSlice(4));
        read.release();

        read = channel.readInbound();
        assertEquals(read, buf.readSlice(4));
        read.release();

        read = channel.readInbound();
        assertNull(read);
    }
}
