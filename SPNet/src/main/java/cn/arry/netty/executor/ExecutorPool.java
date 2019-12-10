package cn.arry.netty.executor;

import cn.arry.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorPool {
    private String poolName;
    private ExecutorService service;
    private int maximumPoolSize;

    public ExecutorPool(String poolName, int maximumPoolSize) {
        service = Executors.newFixedThreadPool(maximumPoolSize, new NamedThreadFactory(poolName));
        this.poolName = poolName;
        this.maximumPoolSize = maximumPoolSize;
    }

    public void submit(Runnable task) {
        service.submit(task);
    }

    /**
     * 停止
     */
    public void stop() {
        try {
            Log.info("Exceutor Closing " + poolName + "...");

            service.shutdown();
            service.awaitTermination(2, TimeUnit.MINUTES);

            Log.info("Exceutor " + poolName + " closed");
        } catch (Exception e) {
            Log.error("Close executor error", e);
        }
    }

    public String getPoolName() {
        return poolName;
    }

    public ExecutorService getService() {
        return service;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }
}
