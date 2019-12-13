package cn.arry;

import cn.arry.meta.ServerConfig;
import cn.arry.netty.component.NettyServerComponent;
import cn.arry.netty.handler.ProxyChannelHandler;
import cn.arry.netty.handler.cmd.ProxyCmdHandler;
import cn.arry.netty.initializer.ServerChannelInitializer;
import cn.arry.netty.manager.CmdManager;
import cn.arry.netty.manager.ConfigManager;
import cn.arry.type.ServerType;

import java.util.List;
import java.util.Map;

/**
 * proxy server
 * server's server
 * 转发服，只用处理服务器消息，对于server来说是服务器
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
public class ProxyServer {
    public static void main(String[] args) {
        if (!ConfigManager.getInstance().init("../resources/config/variable.xml")) {
            return;
        }

        if (!CmdManager.init("cn.arry.cmd.proxy", null)) {
            return;
        }

        // 服务器配置
        Map<Integer, List<ServerConfig>> serverMap = ConfigManager.getInstance().getServerConfigMap();

        // 充当server角色
        ServerConfig serverConfig = serverMap.get(ServerType.PROXY.getValue()).get(0);
        if (NettyServerComponent.getInstance().start(serverConfig.getAddr(), serverConfig.getPort(),
                new ServerChannelInitializer(new ProxyChannelHandler(new ProxyCmdHandler())))) {
            return;
        }

        Log.info("ProxyServer has started successfully");
    }
}
