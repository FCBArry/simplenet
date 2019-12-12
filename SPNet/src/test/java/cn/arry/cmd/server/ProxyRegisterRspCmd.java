package cn.arry.cmd.server;

import cn.arry.CmdType;
import cn.arry.Log;
import cn.arry.gen.server.SCommonMsg.ServerRegisterSuccessProto;
import cn.arry.netty.cmd.CommandAdapter;
import cn.arry.netty.cmd.ICode;
import cn.arry.netty.component.ProxyConnComponent;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.connection.NettyServerConnection;
import cn.arry.netty.packet.S2SPacket;

/**
 * 服务器注册返回
 */
@ICode(code = CmdType.S2S_SERVER_LOGIN_RESP, desc = "服务器注册返回")
public class ProxyRegisterRspCmd extends CommandAdapter {
    @Override
    public void execute(AbstractConnection session, S2SPacket packet) throws Exception {
        ServerRegisterSuccessProto proto = ServerRegisterSuccessProto.parseFrom(packet.getBodyData());
        if (!proto.getIsSuccess()) {
            Log.error("server to proxy register fail, serverId:{}", proto.getServerId());
            return;
        }

        NettyServerConnection serverConnection = (NettyServerConnection) session;
        serverConnection.setServerId(proto.getServerId());
        ProxyConnComponent.getInstance().putProxyClient(serverConnection);
        Log.info("server to proxy register success, serverId:{}", proto.getServerId());
    }
}
