package cn.arry;

import cn.arry.netty.component.NettyServerComponent;
import cn.arry.netty.handler.ProxyChannelHandler;
import cn.arry.netty.handler.cmd.ProxyCmdHandler;
import cn.arry.netty.initializer.ServerChannelInitializer;
import cn.arry.netty.manager.CmdManager;
import cn.arry.redis.RedisMgr;
import cn.arry.redis.pool.Node;
import cn.arry.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

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
        if (!FileUtil.loadDefaultLogbackFile()) {
            return;
        }

        if (!CmdManager.init("cn.arry.cmd.proxy", null)) {
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
        if (NettyServerComponent.getInstance().start("0.0.0.0", 8888,
                new ServerChannelInitializer(new ProxyChannelHandler(new ProxyCmdHandler())))) {
            return;
        }

        Log.info("ProxyServer has started successfully");
    }
}
