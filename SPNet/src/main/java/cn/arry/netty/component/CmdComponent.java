package cn.arry.netty.component;

import cn.arry.Log;
import cn.arry.netty.cmd.CmdTask;
import cn.arry.netty.cmd.ICommand;
import cn.arry.netty.connection.AbstractConnection;
import cn.arry.netty.executor.CmdTaskQueue;
import cn.arry.netty.executor.ExecutorPool;
import cn.arry.netty.packet.Packet;

public class CmdComponent implements IComponent {
    /**
     * 线程池
     */
    private ExecutorPool cmdPool;

    /**
     * 指令任务队列
     */
    private CmdTaskQueue[] queue;

    private static class LazyHolder {
        private static final CmdComponent INSTANCE = new CmdComponent();
    }

    public static CmdComponent getInstance() {
        return LazyHolder.INSTANCE;
    }

    public CmdComponent() {
        init();
    }

    private void init() {
        int core = Runtime.getRuntime().availableProcessors();
        cmdPool = new ExecutorPool("cmd-pool", core * 2 + 1);

        queue = new CmdTaskQueue[cmdPool.getNThreads()];
        for (int i = 0; i < queue.length; i++) {
            queue[i] = new CmdTaskQueue(cmdPool);
        }
    }

    /**
     * 消息处理
     */
    public void handle(AbstractConnection session, Packet packet, ICommand cmd) {
        if (cmd == null) {
            Log.error("CmdComponent->handle, can not find cmd:{}", packet.getCode());
        }

        CmdTask cmdTask = new CmdTask(cmd, session, packet);
        if (packet.getDestUserID() > 0) {
            queue[(int) (packet.getDestUserID() % queue.length)].enqueue(cmdTask);
        } else {
            cmdPool.submit(cmdTask);
        }
    }

    public void stop() {
        if (cmdPool != null) {
            cmdPool.stop();
        }
    }
}
