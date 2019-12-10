package cn.arry.netty.packet;

import com.google.protobuf.GeneratedMessage.Builder;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
@Getter
@Setter
public abstract class Packet {
    /**
     * 协议号
     */
    protected short code;

    /**
     * 包体
     */
    protected byte[] bodyData;

    public Packet() {

    }

    public Packet(short code) {
        this.code = code;
    }

    /**
     * 解码read
     */
    public abstract Packet read(ByteBuf in);

    /**
     * 编码write
     */
    public abstract void write(ByteBuf out);

    /**
     * 获取包长
     */
    public abstract int getLength();

    /**
     * 设置pb数据
     */
    public void setBuilder(Builder<?> builder) {
        this.bodyData = builder.build().toByteArray();
    }
}
