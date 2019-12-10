package cn.arry.netty.executor;

public class CmdTaskQueue extends TaskQueue<ExecutorPool, Runnable> {
    public CmdTaskQueue(ExecutorPool executor) {
        super(executor);
    }
}
