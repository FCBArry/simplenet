package cn.arry.netty.cmd;

import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.packet.Packet;

/**
 * 指令接口
 */
public interface ICommand {
    void execute(AbstractConnection session, Packet packet) throws Exception;
}
