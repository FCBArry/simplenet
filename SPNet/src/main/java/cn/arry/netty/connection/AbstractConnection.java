package cn.arry.netty.connection;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

/**
 * 链接对象抽象类
 */
@Setter
@Getter
public abstract class AbstractConnection {
    /**
     * 连接对象
     */
    protected Channel channel;

    public AbstractConnection() {

    }

    public AbstractConnection(Channel channel) {
        this.channel = channel;
    }

    /**
     * 是否连接
     */
    public abstract boolean isConnected();

    /**
     * 断开连接
     */
    public abstract void disconnect();

    /**
     * 发送数据包
     */
    public void send(Object packet) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(packet);
        }
    }
}
