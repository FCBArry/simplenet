package cn.arry.cmd.client;

import cn.arry.CmdType;
import cn.arry.Log;
import cn.arry.gen.client.CCommonMsg.SayHelloProto;
import cn.arry.netty.cmd.CommandAdapter;
import cn.arry.netty.cmd.ICode;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.connection.NettyClientConnection;
import cn.arry.netty.packet.C2SPacket;

/**
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
@ICode(code = CmdType.C2S_SAY_HELLO, desc = "c2s say hello")
public class C2SSayHelloCmd extends CommandAdapter {
    @Override
    public void execute(AbstractConnection session, C2SPacket packet) throws Exception {
        // 收
        SayHelloProto proto = SayHelloProto.parseFrom(packet.getBodyData());
        Log.info("C2SSayHelloCmd->execute, {} {} {}", packet.getCode(), packet.getDestUserID(), proto.getNum());

        // 发
        NettyClientConnection nettyClientConnection = (NettyClientConnection) session;
        nettyClientConnection.sendClientMessage(CmdType.C2S_SAY_HELLO, proto.toBuilder());
    }
}
