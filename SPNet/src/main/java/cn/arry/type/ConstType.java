package cn.arry.type;

import cn.arry.netty.connection.NettyClientConnection;
import cn.arry.netty.connection.NettyServerConnection;
import io.netty.util.AttributeKey;

/**
 * 常量类
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/10
 */
public class ConstType {
    /**
     * 客户端链接
     */
    public static final AttributeKey<NettyClientConnection> CLIENT_SESSION = AttributeKey.valueOf("CLIENT_SESSION");

    /**
     * 服务器链接
     */
    public static final AttributeKey<NettyServerConnection> SERVER_SESSION = AttributeKey.valueOf("SERVER_SESSION");

    public static final int BasePercentIntNumber = 10000;

    public static final int TEMP_GAME_SERVER_ID = 1001;

    public static final int TEMP_PROXY_SERVER_ID = 2001;

    public static final String LOGBACK_FILE_PATH = "../resources/config/logback.xml";
}
