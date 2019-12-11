package cn.arry.netty.executor;

import cn.arry.Log;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务队列
 * <p>
 * TODO 考虑用LinkedBlockingQueue实现
 *
 * @see LinkedBlockingQueue
 */
@Getter
public class TaskQueue<E extends ExecutorPool, R extends Runnable> {
    private E executor;

    private Queue<Runnable> queue;

    public TaskQueue(E executor) {
        this.executor = executor;
        queue = new LinkedList<>();
    }

    /**
     * 加入队列
     */
    public void enqueue(final R task) {
        // 任务封装
        Runnable task0 = new Runnable() {
            public void run() {
                try {
                    task.run();
                } catch (Throwable e) {
                    Log.error("task execute error", e);
                }

                // 从队列中移除自己
                TaskQueue.this.dequeue(this);
            }
        };

        // 加入消息
        int queueSize;
        synchronized (queue) {
            queue.add(task0);
            queueSize = queue.size();
        }

        // 如果消息数量为1个
        if (queueSize == 1) {
            // 立马执行
            executor.submit(task0);
        } else if (queueSize > 1000) {
            // 提醒消息累计过多
            Log.warn("{} queue.size > 1000, curSize:{}" + executor.getPoolName(), queueSize);
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
                Log.error("task queue is empty");
            }

            // 移除队列头
            Runnable temp = queue.remove();
            if (temp != task) {
                // 消息不对，报错
                Log.error("task is not the first, cur:{} first:{}", task, temp);
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
}
