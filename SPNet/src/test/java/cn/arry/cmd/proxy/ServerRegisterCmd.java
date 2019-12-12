package cn.arry.cmd.proxy;

import cn.arry.CmdType;
import cn.arry.Const;
import cn.arry.gen.server.SCommonMsg;
import cn.arry.netty.cmd.CommandAdapter;
import cn.arry.netty.cmd.ICode;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.connection.NettyServerConnection;
import cn.arry.netty.manager.ServerClientManager;
import cn.arry.netty.packet.S2SPacket;

/**
 * 服务器注册协议
 */
@ICode(code = CmdType.S2S_SERVER_LOGIN, desc = "服务器注册")
public class ServerRegisterCmd extends CommandAdapter {
    @Override
    public void execute(AbstractConnection session, S2SPacket packet) throws Exception {
        SCommonMsg.ServerRegisterProto proto = SCommonMsg.ServerRegisterProto.parseFrom(packet.getBodyData());
        NettyServerConnection nettyServerConnection = (NettyServerConnection) session;
        nettyServerConnection.setServerId(proto.getServerId());
        ServerClientManager.addServerClient(nettyServerConnection);

        // 返回注册成功
        int serverId = Const.TEMP_PROXY_SERVER_ID;
        SCommonMsg.ServerRegisterSuccessProto.Builder builder = SCommonMsg.ServerRegisterSuccessProto.newBuilder();
        builder.setServerId(serverId);
        builder.setIsSuccess(true);
        nettyServerConnection.sendServerMessage(CmdType.S2S_SERVER_LOGIN_RESP, builder);
    }
}
