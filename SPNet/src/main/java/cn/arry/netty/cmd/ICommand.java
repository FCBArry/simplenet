package cn.arry.netty.cmd;

import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.packet.Packet;

public interface ICommand {
    void execute(AbstractConnection session, Packet packet) throws Exception;
}
