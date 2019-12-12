package cn.arry.netty.component;

import cn.arry.Log;
import cn.arry.meta.ServerConfig;
import cn.arry.netty.connection.NettyServerConnection;
import cn.arry.netty.handler.cmd.CommonCmdHandler;
import cn.arry.netty.packet.S2SPacket;
import com.google.protobuf.GeneratedMessage.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 转发服链接对象
 */
public class ProxyConnComponent implements IComponent {
    private static class LazyHolder {
        private static final ProxyConnComponent INSTANCE = new ProxyConnComponent();
    }

    public static ProxyConnComponent getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 断线重连的线程池
     */
    private ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(1);

    /**
     * 转发服链接对象，key：serverId
     */
    private Map<Integer, NettyServerConnection> proxyMap = new ConcurrentHashMap<>();

    public void putProxyClient(NettyServerConnection connector) {
        proxyMap.put(connector.getServerId(), connector);
    }

    private CommonCmdHandler cmdHandler;

    private List<ServerConfig> serverConfigs;

    public boolean start(CommonCmdHandler cmdHandler, List<ServerConfig> serverConfigs) {
        this.cmdHandler = cmdHandler;
        this.serverConfigs = serverConfigs;

        reloadConnect();
        return true;
    }

    @Override
    public void stop() {
        if (proxyMap != null) {
            for (NettyServerConnection connection : proxyMap.values()) {
                connection.setAutoConnect(false);
                connection.disconnect();
            }
        }
    }

    public boolean reloadConnect() {
        for (ServerConfig config : serverConfigs) {
            NettyServerConnection client = proxyMap.get(config.getServerId());
            if (client == null) {
                createConnector(config);
            }
        }

        return true;
    }

    /**
     * 创建一个链接对象
     */
    private void createConnector(ServerConfig config) {
        NettyServerConnection client = new NettyServerConnection(config.getAddr(), config.getPort(), cmdHandler);
        client.setService(service);
        client.reconnect();
    }

    /**
     * 随机一个转发服链接对象
     */
    public NettyServerConnection randomConnection() {
        if (proxyMap == null || proxyMap.isEmpty()) {
            Log.error("ProxyConnComponent->randomConnector null");
            return null;
        }

        List<NettyServerConnection> connectionList = new ArrayList<>();
        connectionList.addAll(proxyMap.values());

        Collections.shuffle(connectionList);

        for (NettyServerConnection connection : connectionList) {
            if (connection.getIsOpen() <= 0) {
                continue;
            }

            return connection;
        }

        Log.error("ProxyConnComponent->randomConnector null no useful Connection");
        return null;
    }

    /**
     * server包
     */
    public void sendServerMessage(short code, Builder<?> builder) {
        try {
            NettyServerConnection nettyServerConnection = randomConnection();
            if (nettyServerConnection == null)
                return;

            nettyServerConnection.sendServerMessage(code, builder);
        } catch (Exception e) {
            Log.error("ProxyConnComponent->sendServerMessage code:{}", code, e);
        }
    }

    public void sendServerMessage(S2SPacket s2SPacket) {
        NettyServerConnection nettyServerConnection = randomConnection();
        if (nettyServerConnection == null)
            return;

        nettyServerConnection.send(s2SPacket);
    }

    /**
     * server包->发送给特定玩家
     */
    public void sendServerMessage(short code, Builder<?> builder, long userId) {
        try {
            NettyServerConnection nettyServerConnection = randomConnection();
            if (nettyServerConnection == null)
                return;

            nettyServerConnection.sendServerMessage(code, builder, userId);
        } catch (Exception e) {
            Log.error("ProxyConnComponent->sendServerMessage code:{}", code, e);
        }
    }

    /**
     * server包->发送给特定服务器
     */
    public void sendServerMessage(short code, Builder<?> builder, int destServerId) {
        try {
            NettyServerConnection nettyServerConnection = randomConnection();
            if (nettyServerConnection == null)
                return;

            nettyServerConnection.sendServerMessage(code, builder, destServerId);
        } catch (Exception e) {
            Log.error("ProxyConnComponent->sendServerMessage code:{}", code, e);
        }
    }
}
