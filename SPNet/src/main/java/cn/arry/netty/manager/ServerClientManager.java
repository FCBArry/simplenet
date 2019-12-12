package cn.arry.netty.manager;

import cn.arry.Log;
import cn.arry.netty.connection.NettyServerConnection;
import cn.arry.type.ServerType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器链接管理类
 */
public class ServerClientManager {
    /**
     * 链接对象，key1为服务器类型，key2为serverId
     */
    private static Map<Integer, Map<Integer, NettyServerConnection>> connectorMap = new ConcurrentHashMap<>();

    /**
     * 增加链接
     */
    public static void addServerClient(NettyServerConnection proxyServerClient) {
        ServerType serveType = ServerType.getTypeByServerId(proxyServerClient.getServerId());
        connectorMap.putIfAbsent(serveType.getValue(), new HashMap<>());
        proxyServerClient.setServerType(serveType.getValue());

        connectorMap.get(serveType.getValue()).put(proxyServerClient.getServerId(), proxyServerClient);
        ServerRedisManager.setServerInfo(serveType.getValue(), proxyServerClient.getServerId());
        Log.info("serverRegisterProxy -> addServerClient, serverType:{}, serverName:{}, serverId:{}",
                serveType.getValue(), serveType.getName(), proxyServerClient.getServerId());
    }

    /**
     * 移除链接
     */
    public static void removeServerClient(NettyServerConnection proxyServerClient) {
        ServerType serveType = ServerType.getTypeByServerId(proxyServerClient.getServerId());
        if (connectorMap.containsKey(serveType.getValue())) {
            connectorMap.get(serveType.getValue()).remove(proxyServerClient.getServerId());
        }

        ServerRedisManager.removeServerInfo(serveType.getValue(), proxyServerClient.getServerId());
        Log.info("removeServerClient, serverType:{}, serverName:{}, serverId:{}",
                serveType.getValue(), serveType.getName(), proxyServerClient.getServerId());
    }

    /**
     * 获取某一服务器类型的连接
     */
    public static Map<Integer, NettyServerConnection> getConnectors(int serverType) {
        return connectorMap.get(serverType);
    }

    /**
     * 获取单个连接
     */
    public static NettyServerConnection getConnector(int serverType, int serverId) {
        Map<Integer, NettyServerConnection> map = connectorMap.get(serverType);
        if (map == null) {
            return null;
        }

        return map.get(serverId);
    }
}
