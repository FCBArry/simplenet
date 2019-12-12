package cn.arry;

import cn.arry.netty.component.NettyServerComponent;
import cn.arry.netty.component.ProxyConnComponent;
import cn.arry.netty.handler.ClientChannelHandler;
import cn.arry.netty.handler.cmd.CommonCmdHandler;
import cn.arry.netty.initializer.ClientChannelInitializer;
import cn.arry.netty.manager.CmdManager;
import cn.arry.redis.RedisMgr;
import cn.arry.redis.pool.Node;
import cn.arry.type.ServerConfig;
import cn.arry.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

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
        if (!FileUtil.loadDefaultLogbackFile()) {
            return;
        }

        if (!CmdManager.init("cn.arry.cmd.client", "cn.arry.cmd.server")) {
            return;
        }

        List<Node> nodes = new ArrayList<>();
        Node node = new Node();
        node.setIp("127.0.0.1");
        node.setPort(6379);
        node.setAuth("");
        node.setTimeout(1000000);
        node.setZkProxyDir("/jodis/sdtest");
        node.setDb(1);
        nodes.add(node);
        if (!RedisMgr.init(1, nodes)) {
            return;
        }

        // 充当server角色
        if (!NettyServerComponent.getInstance().start("0.0.0.0", 6666,
                new ClientChannelInitializer(new ClientChannelHandler(new CommonCmdHandler())))) {
            return;
        }

        // 充当client角色
        List<ServerConfig> serverConfigs = new ArrayList<>();
        ServerConfig serverConfig = new ServerConfig(2001, "127.0.0.1", 8888);
        serverConfigs.add(serverConfig);
        if (!ProxyConnComponent.getInstance().start(new CommonCmdHandler(), serverConfigs)) {
            return;
        }

        Log.info("TestServer has started successfully");
    }
}
