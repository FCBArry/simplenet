package cn.arry.netty.cmd;

import cn.arry.Log;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.packet.Packet;

/**
 * command task
 */
public class CmdTask implements Runnable {
    private ICommand cmd;

    private AbstractConnection session;

    private Packet packet;

    public CmdTask(ICommand cmd, AbstractConnection session, Packet packet) {
        this.cmd = cmd;
        this.session = session;
        this.packet = packet;
    }

    public void run() {
        short code = (packet != null) ? packet.getCode() : -1;
        try {
            long start = System.currentTimeMillis();
            cmd.execute(session, packet);
            long end = System.currentTimeMillis();
            long interval = end - start;
            if (interval > 500) {
                String cmdName = cmd.getClass().getName();
                Log.warn("cmd task run too long time, cmd:{} code:{} interval:{}", cmdName, code, interval);
            }
        } catch (Throwable e) {
            Log.error("CmdTask run error, cmd:{} code:{}", cmd.getClass().getName(), code, e);
        }
    }

    @Override
    public String toString() {
        return "CmdTask [cmd=" + cmd + ", session=" + session.getClass().getSimpleName() + "]";
    }
}
