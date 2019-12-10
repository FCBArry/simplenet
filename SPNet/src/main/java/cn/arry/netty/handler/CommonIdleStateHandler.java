package cn.arry.netty.handler;

import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 心跳检测
 *
 * @author 云顶之弈江流儿
 * @version 2019/10/29
 */
public class CommonIdleStateHandler extends IdleStateHandler {
    public static final int READ_IDLE_TIME = 60 * 60;

    public static final int WRITE_IDLE_TIME = 60 * 60;

    public static final int ALL_IDLE_TIME = 60 * 60;

    public CommonIdleStateHandler() {
        super(READ_IDLE_TIME, WRITE_IDLE_TIME, ALL_IDLE_TIME, TimeUnit.SECONDS);
    }
}
