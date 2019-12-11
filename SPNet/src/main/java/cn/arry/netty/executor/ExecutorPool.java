package cn.arry.netty.executor;

import cn.arry.Log;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 任务线程池
 */
@Getter
public class ExecutorPool {
    private String poolName;

    private ExecutorService service;

    private int nThreads;

    public ExecutorPool(String poolName, int nThreads) {
        service = Executors.newFixedThreadPool(nThreads, new NamedThreadFactory(poolName));
        this.poolName = poolName;
        this.nThreads = nThreads;
    }

    public void submit(Runnable task) {
        service.submit(task);
    }

    public void stop() {
        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            Log.error("ExecutorPool stop error", e);
        }
    }
}
