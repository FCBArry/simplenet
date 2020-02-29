package cn.arry.netty.connection;

/**
 * 连接持有者接口
 */
public interface IConnectionHolder {
    /**
     * 断线
     */
    void onDisconnect();
}
