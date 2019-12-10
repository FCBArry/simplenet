package cn.arry.netty.executor;

import cn.arry.Log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory, UncaughtExceptionHandler {
    /**
     * 线程名，便于后期查bug使用
     */
    private String threadName;

    public NamedThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.threadName);
        t.setDaemon(false);
        t.setUncaughtExceptionHandler(this);
        return t;
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.error("Uncaught Exception in thread " + thread.getName(), throwable);
    }
}
