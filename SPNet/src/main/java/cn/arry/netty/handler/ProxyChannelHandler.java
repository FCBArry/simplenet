package cn.arry.netty.handler;

import cn.arry.Log;
import cn.arry.netty.connection.NettyServerConnection;
import cn.arry.netty.handler.cmd.CommonCmdHandler;
import cn.arry.netty.manager.ServerClientManager;
import cn.arry.type.ConstType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author 科兴第一盖伦
 * @version 2019/5/29
 */
public class ProxyChannelHandler extends ServerChannelHandler {
    public ProxyChannelHandler(CommonCmdHandler cmdHandler) {
        super(cmdHandler);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("ProxyChannelHandler->channelInactive, server socket disconnect, address:{} channel{}",
                ctx.channel().remoteAddress(), ctx.hashCode());
        NettyServerConnection serverConnection = ctx.channel().attr(ConstType.SERVER_SESSION).get();
        if (serverConnection != null) {
            serverConnection.disconnect();
            ServerClientManager.removeServerClient(serverConnection);
        }
    }
}
