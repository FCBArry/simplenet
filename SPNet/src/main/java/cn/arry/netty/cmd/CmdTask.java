package cn.arry.netty.cmd;

import cn.arry.Log;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.packet.Packet;

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
        short code = (packet != null) ? packet.getCode() : 0;
        try {
            long start = System.currentTimeMillis();
            cmd.execute(session, packet);
            long end = System.currentTimeMillis();
            long interval = end - start;
            if (interval > 500) {
                String cmdName = cmd.getClass().getName();
                Log.warn("Execute cmd took long time, cmd : " + cmdName + ", code : " + code + ", interval : " + interval);
            }
        } catch (Throwable e) {
            Log.error("CmdTask catch a throwable ! cmd : " + cmd.getClass().getName() + " code : " + code, e);
        }
    }

    @Override
    public String toString() {
        return "CmdTask [cmd=" + cmd + ", session=" + session.getClass().getSimpleName() + "]";
    }
}
