package cn.arry.netty.connection;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;

/**
 * 链接对象抽象类
 */
@Setter
@Getter
public abstract class AbstractConnection {
    /**
     * 连接的服务器id
     */
    protected int serverId;

    /**
     * 链接服务器类型
     */
    protected int serverType;

    /**
     * 连接对象
     */
    protected Channel channel;

    /**
     * 客户端地址
     */
    protected String clientIp;

    public AbstractConnection() {

    }

    public AbstractConnection(Channel channel) {
        this.channel = channel;
        InetSocketAddress inSocket = (InetSocketAddress) channel.remoteAddress();
        this.clientIp = inSocket.getAddress().getHostAddress();
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
