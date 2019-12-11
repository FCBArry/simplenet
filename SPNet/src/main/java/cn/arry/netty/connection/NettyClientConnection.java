package cn.arry.netty.connection;

import cn.arry.netty.packet.C2SPacket;
import com.google.protobuf.GeneratedMessage.Builder;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

/**
 * c2s链接对象
 */
@Getter
@Setter
public class NettyClientConnection extends AbstractConnection {
    /**
     * 连接持有者
     */
    private IConnectionHolder holder;

    public NettyClientConnection(Channel channel) {
        super(channel);
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public void disconnect() {
        if (holder != null) {
            holder.onDisconnect();
        }
    }

    public synchronized void setHolder(IConnectionHolder holder) {
        this.holder = holder;
    }

    /**
     * client包
     */
    public void sendClientMessage(short code, Builder<?> builder) {
        C2SPacket packet = new C2SPacket(code);
        if (builder != null)
            packet.setBuilder(builder);

        send(packet);
    }
}
