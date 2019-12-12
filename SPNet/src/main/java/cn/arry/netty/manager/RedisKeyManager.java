package cn.arry.netty.manager;

/**
 * redis key管理
 */
public class RedisKeyManager {
    /**
     * 服务器id信息
     */
    public static byte[] getServerIds(int serverType) {
        return String.format("Server:%d:ServerIds", serverType).getBytes();
    }
}
