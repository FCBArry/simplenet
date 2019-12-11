package cn.arry.netty.handler;

import cn.arry.Log;
import cn.arry.netty.cmd.ICommand;
import cn.arry.netty.component.CmdComponent;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.manager.CmdManager;
import cn.arry.netty.packet.Packet;

public class CommonCmdHandler {
    public void handle(AbstractConnection session, Packet packet) {
        ICommand command = getCommand(packet.getCode());
        if (command == null) {
            return;
        }

        CmdComponent.getInstance().handle(session, packet, command);
    }

    public ICommand getCommand(short code) {
        ICommand command = CmdManager.getCommand(code);
        if (command == null) {
            Log.error("CommonCmdHandler->getCode error, code:{}", code);
        }

        return command;
    }
}
