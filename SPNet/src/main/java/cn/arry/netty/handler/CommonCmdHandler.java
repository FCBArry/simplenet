package cn.arry.netty.handler;

import cn.arry.Log;
import cn.arry.netty.cmd.ICommand;
import cn.arry.netty.component.ServerCmdComponent;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.manager.CmdCacheManager;
import cn.arry.netty.packet.Packet;

public class CommonCmdHandler {
    public boolean handlerQueue(AbstractConnection session, Packet packet) {
        ICommand command = getCode(packet.getCode());
        if (command == null) {
            return false;
        }

        return ServerCmdComponent.getInstance().handlerQueue(session, packet, command);
    }

    public ICommand getCode(short code) {
        ICommand command = CmdCacheManager.getCommand(code);
        if (command == null) {
            Log.error("CommonCmdHandler->getCode, code:{}", code);
        }

        return command;
    }
}
