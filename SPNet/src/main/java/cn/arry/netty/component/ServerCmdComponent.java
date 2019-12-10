package cn.arry.netty.component;

import cn.arry.Log;
import cn.arry.netty.cmd.CmdTask;
import cn.arry.netty.cmd.ICommand;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.executor.CmdTaskQueue;
import cn.arry.netty.executor.ExecutorPool;
import cn.arry.netty.packet.Packet;

public class ServerCmdComponent implements IComponent {
    private static class LazyHolder {
        private static final ServerCmdComponent INSTANCE = new ServerCmdComponent();
    }

    public static ServerCmdComponent getInstance() {
        return LazyHolder.INSTANCE;
    }

    public ServerCmdComponent() {
        init();
    }

    // 指令集合
    private ExecutorPool cmdPool;

    // 指令队列，其中一个是玩家队列
    private CmdTaskQueue[] queue;

    /**
     * 处理队列，一般处理玩家消息
     */
    public boolean handlerQueue(AbstractConnection session, Packet packet, ICommand cmd) {
        if (cmd == null) {
            Log.error("Can't find cmd, code:{}", packet.getCode());
            return false;
        }

        if (packet.getDestUserID() > 0) {
            queue[(int) (packet.getDestUserID() % queue.length)].enqueue(new CmdTask(cmd, session, packet));
        } else {
            cmdPool.submit(new CmdTask(cmd, session, packet));
        }

        return true;
    }

    /**
     * 初始化cmd集合
     */
    public boolean init() {
        try {
            //cmd的驱动线程
            int core = Runtime.getRuntime().availableProcessors();
            cmdPool = new ExecutorPool("game-player-cmd-pool", core * 4);

            queue = new CmdTaskQueue[cmdPool.getMaximumPoolSize()];
            for (int i = 0; i < queue.length; i++) {
                queue[i] = new CmdTaskQueue(cmdPool);
            }
        } catch (Exception e) {
            Log.error("ServerCmdComponent error packageName:{}", e);
            return false;
        }

        return true;
    }

    public void stop() {
        if (cmdPool != null) {
            cmdPool.stop();
        }
    }
}
