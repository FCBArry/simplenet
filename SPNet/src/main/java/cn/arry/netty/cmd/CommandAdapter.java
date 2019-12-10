package cn.arry.netty.cmd;

import cn.arry.Log;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.packet.C2SPacket;
import cn.arry.netty.packet.Packet;
import cn.arry.netty.packet.S2SPacket;

public class CommandAdapter implements ICommand {
    @Override
    public void execute(AbstractConnection session, Packet packet) throws Exception {
        try {
            if (packet instanceof C2SPacket) {
                execute(session, (C2SPacket) packet);
            } else if (packet instanceof S2SPacket) {
                execute(session, (S2SPacket) packet);
            }
        } catch (Exception e) {
            Log.error("CommandAdapter->execute error", e);
        }
    }

    public void execute(AbstractConnection session, C2SPacket packet) throws Exception {
        Log.info("code {} unimplemented", packet.getCode());
    }

    public void execute(AbstractConnection session, S2SPacket packet) throws Exception {
        Log.info("code {} unimplemented", packet.getCode());
    }
}
