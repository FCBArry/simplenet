package cn.arry;

import cn.arry.netty.connection.NettyClientConnection;
import cn.arry.netty.connection.NettyServerConnection;
import io.netty.util.AttributeKey;

/**
 * 常量类
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
public class Const {
    /**
     * 客户端链接
     */
    public static final AttributeKey<NettyClientConnection> CLIENT_SESSION = AttributeKey.valueOf("CLIENT_SESSION");

    /**
     * 服务器链接
     */
    public static final AttributeKey<NettyServerConnection> SERVER_SESSION = AttributeKey.valueOf("SERVER_SESSION");
}
