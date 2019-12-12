package cn.arry.netty.handler.cmd;

import cn.arry.CmdType;
import cn.arry.netty.cmd.ICommand;
import cn.arry.netty.manager.CmdManager;

/**
 * proxy cmd handler
 */
public class ProxyCmdHandler extends CommonCmdHandler {
    @Override
    public ICommand getCommand(short code) {
        ICommand cmd = CmdManager.getCommand(code);
        if (cmd == null) {
            cmd = CmdManager.getCommand(CmdType.S2S_SERVER_COMMON);
        }

        return cmd;
    }
}
