package cn.arry.netty.connection;

public interface IConnectionHolder {
    /**
     * 连接关闭时的回调
     */
    void onDisconnect();

    /**
     * 玩家主动退出
     */
    void onExit();
}
