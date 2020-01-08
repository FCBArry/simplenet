package cn.arry;

import cn.arry.meta.ServerConfig;
import cn.arry.netty.component.tcp.NettyServerComponent;
import cn.arry.netty.component.tcp.ProxyConnComponent;
import cn.arry.netty.handler.ClientChannelHandler;
import cn.arry.netty.handler.cmd.CommonCmdHandler;
import cn.arry.netty.initializer.ClientChannelInitializer;
import cn.arry.netty.manager.CmdManager;
import cn.arry.netty.manager.ConfigManager;
import cn.arry.type.ServerType;

import java.util.List;
import java.util.Map;

/**
 * server
 * client ---> server -> proxy
 * server既要处理client的消息又要处理proxy的消息
 * 对于client来说，server是服务器，对于proxy来说，server是客户端
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
public class TestServer {
    public static void main(String[] args) {
        if (!ConfigManager.getInstance().init("../resources/config/variable.xml")) {
            return;
        }

        if (!CmdManager.init("cn.arry.cmd.client", "cn.arry.cmd.server")) {
            return;
        }

        // 服务器配置
        Map<Integer, List<ServerConfig>> serverMap = ConfigManager.getInstance().getServerConfigMap();

        // 充当server角色
        ServerConfig serverConfig = serverMap.get(ServerType.GAME.getValue()).get(0);
        if (!NettyServerComponent.getInstance().start(serverConfig.getAddr(), serverConfig.getPort(),
                new ClientChannelInitializer(new ClientChannelHandler(new CommonCmdHandler())))) {
            return;
        }

        // 充当client角色
        List<ServerConfig> serverConfigList = serverMap.get(ServerType.PROXY.getValue());
        if (!ProxyConnComponent.getInstance().start(new CommonCmdHandler(), serverConfigList)) {
            return;
        }

        Log.info("TestServer has started successfully");
    }
}
