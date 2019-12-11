package cn.arry;

import cn.arry.netty.component.NettyServerComponent;
import cn.arry.netty.handler.ClientChannelHandler;
import cn.arry.netty.handler.CommonCmdHandler;
import cn.arry.netty.initializer.ClientChannelInitializer;
import cn.arry.netty.manager.CmdManager;
import cn.arry.utils.FileUtil;

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

        if (!CmdManager.init("cn.arry.cmd", null)) {
            return;
        }

        // 充当server角色
        NettyServerComponent.getInstance().start("0.0.0.0", 6666,
                new ClientChannelInitializer(new ClientChannelHandler(new CommonCmdHandler())));

        // 充当client角色
    }
}
