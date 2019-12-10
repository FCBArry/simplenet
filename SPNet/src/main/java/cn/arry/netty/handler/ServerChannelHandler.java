package cn.arry.netty.handler;

import cn.arry.Const;
import cn.arry.Log;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.connection.NettyServerConnection;
import cn.arry.netty.packet.S2SPacket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<S2SPacket> {
    private CommonCmdHandler cmdHandler;

    public ServerChannelHandler(CommonCmdHandler cmdHandler) {
        this.cmdHandler = cmdHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().attr(Const.SERVER_SESSION).get() == null) {
            NettyServerConnection session = new NettyServerConnection(ctx.channel());
            ctx.channel().attr(Const.SERVER_SESSION).set(session);
            Log.info("ServerChannelHandler->channelActive server socket connect, address:{} channel:{} sessionId:{}",
                    ctx.channel().remoteAddress(), ctx.hashCode(), ctx.channel().id());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("server socket disconnect, address:{} channel:{}", ctx.channel().remoteAddress(), ctx.hashCode());
        NettyServerConnection serverSession = ctx.channel().attr(Const.SERVER_SESSION).get();
        serverSession.disconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.warn("server socket exception, address:{}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, S2SPacket packet) throws Exception {
        AbstractConnection session = ctx.channel().attr(Const.SERVER_SESSION).get();
        cmdHandler.handlerQueue(session, packet);
    }
}
