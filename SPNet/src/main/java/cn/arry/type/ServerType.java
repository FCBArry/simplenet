package cn.arry.type;

import cn.arry.Log;

/**
 * 服务器类型
 */
public enum ServerType {
    /**
     * 游戏服
     */
    GAME(1, "GAME"),

    /**
     * 转发服
     */
    PROXY(2, "PROXY");

    private int value;

    private String name;

    ServerType(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据serverId获取对应的ServerType
     */
    public static ServerType getTypeByServerId(int serverId) {
        int serverTypeValue = serverId / 1000;
        ServerType serverType = getTypeByServerTypeValue(serverTypeValue);
        if (serverType == null) {
            Log.error("ServerType->getTypeByServerId error, serverTypeValue:{}", serverTypeValue);
        }

        return serverType;
    }

    public static ServerType getTypeByServerTypeValue(int serverTypeValue) {
        ServerType[] items = ServerType.values();
        for (ServerType item : items) {
            if (item.getValue() == serverTypeValue) {
                return item;
            }
        }

        Log.debug("ServerType->getTypeByServerTypeValue error, serverTypeValue:{}", serverTypeValue);
        return null;
    }
}
