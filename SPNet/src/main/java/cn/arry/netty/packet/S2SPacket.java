package cn.arry.netty.packet;

import cn.arry.Log;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * s2s传输包
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
@Getter
@Setter
public class S2SPacket extends Packet {
    /**
     * 包头长度
     */
    private static final short HEAD_SIZE = 27;

    /**
     * 源userID
     */
    private long sourceUserID;

    /**
     * 目标userID
     */
    private long destUserID;

    /**
     * 目标服务器ID
     */
    private int destServerID;

    /**
     * 是否广播
     */
    private boolean isBroadcast;

    public S2SPacket() {

    }

    public S2SPacket(short code) {
        super(code);
    }

    @Override
    public S2SPacket read(ByteBuf in) {
        try {
            int length = in.readInt();
            this.code = in.readShort();
            this.sourceUserID = in.readLong();
            this.destUserID = in.readLong();
            this.destServerID = in.readInt();
            this.isBroadcast = in.readBoolean();

            int bodyLen = length - HEAD_SIZE;
            if (bodyLen > 0) {
                this.bodyData = new byte[bodyLen];
                in.readBytes(this.bodyData, 0, bodyLen);
            }
        } catch (Exception e) {
            Log.error("S2SPacket->read error, {}", e);
            in.clear();
        }

        return this;
    }

    @Override
    public void write(ByteBuf out) {
        int len = getLength();
        if (len > Short.MAX_VALUE * 0.6) {
            Log.error("C2SPacket->write, len:{} is so max", len);
        }

        try {
            out.writeInt(len);
            out.writeShort(code);
            out.writeLong(sourceUserID);
            out.writeLong(destUserID);
            out.writeInt(destServerID);
            out.writeBoolean(isBroadcast);

            if (bodyData != null) {
                out.writeBytes(bodyData);
            }
        } catch (Exception e) {
            Log.error("S2SPacket->write error, {}", toString(), e);
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
        return "S2SPacket{" +
                "sourceUserID=" + sourceUserID +
                ", destUserID=" + destUserID +
                ", destServerID=" + destServerID +
                ", isBroadcast=" + isBroadcast +
                ", code=" + code +
                '}';
    }
}
