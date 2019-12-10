package cn.arry.netty.executor;

import cn.arry.Log;

import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue<E extends ExecutorPool, R extends Runnable> {
    private E executor;

    private Queue<Runnable> queue;

    public TaskQueue(E executor) {
        this.executor = executor;
        queue = new LinkedList<Runnable>();
    }

    /**
     * 加入队列
     **/
    public void enqueue(final R task) {
        // 任务封装
        Runnable task0 = new Runnable() {
            public void run() {
                try {
                    task.run();
                } catch (Throwable e) {
                    Log.error("Task execute error", e);
                }

                // 从队列中移除自己
                TaskQueue.this.dequeue(this);
            }
        };

        // 加入消息
        int queueSize = 0;
        synchronized (queue) {
            queue.add(task0);
            queueSize = queue.size();
        }

        // 如果消息数量为1个, 说明之前队列已经为空.
        if (queueSize == 1) {
            // 立马执行
            executor.submit(task0);
        } else if (queueSize > 1000) {
            // 提醒消息累计过多
            Log.warn("[" + executor.getPoolName() + "] , queue.size > 1000, curSize: " + queue.size() + ", task: " + task);
        }
    }

    /**
     * 执行完毕, 移除队列
     **/
    private void dequeue(Runnable task) {
        Runnable nextTask = null;
        synchronized (queue) {
            // 判断队列是否为空
            if (queue.isEmpty()) {
                Log.error("Task queue is empty!");
            }

            // 移除队列头
            Runnable temp = queue.remove();
            if (temp != task) {
                // 消息不对, 报错
                Log.error("Task is not the first! cur:" + task + ",first:" + temp);
            }

            // 获取下一个任务
            if (queue.size() != 0) {
                nextTask = queue.peek();
            }
        }

        // 继续执行下一个任务
        if (nextTask != null) {
            executor.submit(nextTask);
        }
    }

    /**
     * 获取队列数量
     **/
    public int getSize() {
        return queue.size();
    }

    public void clear() {
        synchronized (queue) {
            queue.clear();
        }
    }

    public E getExecutor() {
        return executor;
    }
}
