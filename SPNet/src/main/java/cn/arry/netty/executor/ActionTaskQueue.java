package cn.arry.netty.executor;

public class ActionTaskQueue extends TaskQueue<ExecutorPool, Runnable> {
    public ActionTaskQueue(ExecutorPool executor) {
        super(executor);
    }
}
