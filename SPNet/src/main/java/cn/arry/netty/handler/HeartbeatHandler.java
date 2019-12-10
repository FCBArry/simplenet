package cn.arry.netty.handler;

import cn.arry.Log;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 云顶之弈江流儿
 * @version 2019/10/29
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关于不能在日志输出中有at的bug
                // https://stackoverflow.com/questions/7930844/is-it-possible-to-have-clickable-class-names-in-console-output-in-intellij
                Log.info("heart check no message time more than {}s", CommonIdleStateHandler.READ_IDLE_TIME);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
