package cn.arry.netty.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
@Getter
@Setter
public class C2SPacket extends Packet {
    /**
     * 包头长度
     */
    private static final short HEAD_SIZE = 16;

    /**
     * 目标userID
     */
    private long destUserID;

    /**
     * 简单加解密校验sum
     */
    private short edSum;

    public C2SPacket() {

    }

    public C2SPacket(short code) {
        super(code);
    }

    @Override
    public C2SPacket read(ByteBuf in) {
        try {
            int length = in.readInt();
            this.edSum = in.readShort();
            this.code = in.readShort();
            this.destUserID = in.readLong();

            int bodyLen = length - HEAD_SIZE;
            if (bodyLen > 0) {
                this.bodyData = new byte[bodyLen];
                in.readBytes(this.bodyData, 0, bodyLen);

                // 加解密校验sum
                short getCS = calcEdSum(in);
                if (this.edSum != getCS) {
                    return null;
                }
            }
        } catch (Exception e) {

        }

        return this;
    }

    /**
     * 加解密校验sum算法
     */
    private short calcEdSum(ByteBuf in) {
        return 7;
    }

    @Override
    public void write(ByteBuf out) {
        try {
            int length = getLength();
            out.writeInt(length);
            out.writeShort(0);
            out.writeShort(code);
            out.writeLong(destUserID);
            if (bodyData != null) {
                out.writeBytes(bodyData);
            }

            // 加解密校验sum
            int oldPosition = out.writerIndex();
            short sum = calcEdSum(out);
            out.writerIndex(4);
            out.writeShort(sum);
            out.writerIndex(oldPosition);
        } catch (Exception e) {

        }
    }

    @Override
    public int getLength() {
        if (bodyData != null) {
            return HEAD_SIZE + bodyData.length;
        }

        return HEAD_SIZE;
    }

    @Override
    public String toString() {
        return "C2SPacket{" +
                "destUserID=" + destUserID +
                ", edSum=" + edSum +
                ", code=" + code +
                '}';
    }
}
