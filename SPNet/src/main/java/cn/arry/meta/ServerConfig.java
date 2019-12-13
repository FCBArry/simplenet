package cn.arry.meta;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务器配置meta
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/12
 */
@Getter
@Setter
public class ServerConfig {
    private int serverType;

    private int serverId;

    private String addr;

    private int port;

    private int id;

    public ServerConfig(int serverType, int serverId, String addr, int port, int id) {
        this.serverType = serverType;
        this.serverId = serverId;
        this.addr = addr;
        this.port = port;
        this.id = id;
    }
}
