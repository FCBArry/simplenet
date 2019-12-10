package cn.arry.netty.handler;

import cn.arry.Const;
import cn.arry.Log;
import cn.arry.netty.connection.NettyClientConnection;
import cn.arry.netty.packet.Packet;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<Packet> {
    private CommonCmdHandler cmdHandler;

    public ClientChannelHandler(CommonCmdHandler cmdHandler) {
        this.cmdHandler = cmdHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClientConnection session = new NettyClientConnection(ctx.channel());
        ctx.channel().attr(Const.CLIENT_SESSION).set(session);
        Log.info("server socket connect, address:{} channel:{}", ctx.channel().remoteAddress(), ctx.hashCode());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("server socket disconnect, address:{} channel:{}", ctx.channel().remoteAddress(), ctx.hashCode());
        NettyClientConnection clientConnection = ctx.channel().attr(Const.CLIENT_SESSION).get();
        if (clientConnection != null) {
            clientConnection.disconnect();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.warn("server socket exception, address:{}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        NettyClientConnection session = ctx.channel().attr(Const.CLIENT_SESSION).get();
        cmdHandler.handlerQueue(session, packet);
    }
}
