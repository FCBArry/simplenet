package cn.arry.type;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 云顶之弈江流儿
 * @version 2019/12/12
 */
@Getter
@Setter
public class ServerConfig {
    private int serverId;

    private String addr;

    private int port;

    public ServerConfig(int serverId, String addr, int port) {
        this.serverId = serverId;
        this.addr = addr;
        this.port = port;
    }
}
