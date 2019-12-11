package cn.arry.netty.executor;

import cn.arry.Log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * 线程名工厂
 */
public class NamedThreadFactory implements ThreadFactory, UncaughtExceptionHandler {
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
        Log.error("uncaught exception in thread:{}", thread.getName(), throwable);
    }
}
