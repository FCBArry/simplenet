package cn.arry.netty.manager;

import cn.arry.Log;
import cn.arry.gen.common.CommonMsg.ServerIdInfoProto;
import cn.arry.type.ServerType;
import cn.arry.utils.RandomUtil;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡管理器
 *
 * @author 科兴第一盖伦
 * @version 2019/7/10
 */
public class LoadBalanceManager {
    /**
     * 按loadValue最小寻找
     */
    public static int findBestServer(ServerType serverType) {
        int serverId = 0;
        Map<Integer, ServerIdInfoProto> serverMap = ServerRedisManager.getServerIdMap(serverType.getValue());
        if (serverMap == null || serverMap.isEmpty()) {
            Log.error("no useful Server");
            return serverId;
        }

        int minLoadValue = Integer.MAX_VALUE;
        for (ServerIdInfoProto serverInfo : serverMap.values()) {
            if (serverInfo.getLoadValue() < minLoadValue) {
                serverId = serverInfo.getServerId();
                minLoadValue = serverInfo.getLoadValue();
            }
        }

        return serverId;
    }

    /**
     * 随机
     */
    public static int randomServer(ServerType serverType) {
        int serverId = 0;
        Map<Integer, ServerIdInfoProto> serverMap = ServerRedisManager.getServerIdMap(serverType.getValue());
        if (serverMap == null || serverMap.isEmpty()) {
            Log.error("no useful Server");
            return serverId;
        }

        List<ServerIdInfoProto> serverList = (List<ServerIdInfoProto>) serverMap.values();
        return serverList.get(RandomUtil.next(serverList.size())).getServerId();
    }
}
