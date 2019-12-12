package cn.arry.netty.manager;

import cn.arry.Log;
import cn.arry.gen.common.CommonMsg.ServerIdInfoProto;
import cn.arry.redis.RedisMgr;
import cn.arry.type.ServerType;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务器redis管理类
 *
 * @author 科兴第一盖伦
 * @version 2019/5/28
 */
public class ServerRedisManager {
    /**
     * 获取某一类型服务器信息
     */
    public static Map<Integer, ServerIdInfoProto> getServerIdMap(int serverType) {
        byte[] key = RedisKeyManager.getServerIds(serverType);
        Map<byte[], byte[]> data = RedisMgr.hgetAll(key);
        if (data == null)
            return null;

        Map<Integer, ServerIdInfoProto> serverIdMap = new HashMap<>();
        try {
            for (byte[] value : data.values()) {
                ServerIdInfoProto proto = ServerIdInfoProto.parseFrom(value);
                serverIdMap.put(proto.getServerId(), proto);
            }
        } catch (InvalidProtocolBufferException e) {
            Log.error("getServerIdMap error", e);
        }

        return serverIdMap;
    }

    /**
     * 获取单个服务器信息
     */
    public static ServerIdInfoProto getServerInfo(int serverId) {
        ServerType type = ServerType.getTypeByServerId(serverId);
        byte[] key = RedisKeyManager.getServerIds(type.getValue());
        byte[] data = RedisMgr.hget(key, String.valueOf(serverId).getBytes());
        if (data == null)
            return null;

        try {
            return ServerIdInfoProto.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            Log.error("getServerInfo error", e);
        }

        return null;
    }

    /**
     * 设置单个服务器信息, loadValue默认0
     */
    public static void setServerInfo(int serverType, int serverId) {
        setServerInfo(serverType, serverId, 0);
    }

    /**
     * 设置单个服务器信息
     */
    public static void setServerInfo(int serverType, int serverId, int loadValue) {
        ServerIdInfoProto.Builder sb = ServerIdInfoProto.newBuilder();
        sb.setServerId(serverId);
        sb.setLoadValue(loadValue);

        byte[] key = RedisKeyManager.getServerIds(serverType);
        RedisMgr.hset(key, String.valueOf(serverId).getBytes(), sb.build().toByteArray());
    }

    /**
     * 移除单个服务器信息
     */
    public static void removeServerInfo(int serverType, int serverId) {
        byte[] key = RedisKeyManager.getServerIds(serverType);
        RedisMgr.hdel(key, String.valueOf(serverId).getBytes());
    }
}
