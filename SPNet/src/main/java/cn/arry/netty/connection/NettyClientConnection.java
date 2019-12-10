package cn.arry.netty.connection;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NettyClientConnection extends AbstractConnection {
    /**
     * 连接持有者
     */
    private IConnectionHolder holder = null;

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

    public IConnectionHolder getHolder() {
        return holder;
    }

    public synchronized void setHolder(IConnectionHolder holder) {
        this.holder = holder;
    }
}
