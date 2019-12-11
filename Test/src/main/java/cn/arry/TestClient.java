package cn.arry;

import cn.arry.gen.client.CCommonMsg.SayHelloProto;
import cn.arry.netty.component.NettyClientComponent;
import cn.arry.netty.initializer.ClientChannelInitializer;
import cn.arry.netty.packet.C2SPacket;
import cn.arry.utils.FileUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * client
 * client ---> server
 * 一般来说，client由客户端程序来实现
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
public class TestClient {
    public static void main(String[] args) {
        if (!FileUtil.loadDefaultLogbackFile()) {
            return;
        }

        // init client netty
        // 由客户端实现自己的handler处理逻辑
        ChannelHandler handler = new SimpleChannelInboundHandler<C2SPacket>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, C2SPacket c2SPacket)
                    throws Exception {
                if (c2SPacket != null) {
                    // 模拟客户端接收数据
                    SayHelloProto proto = SayHelloProto.parseFrom(c2SPacket.getBodyData());
                    Log.info("client receive msg:{},{}", c2SPacket.getCode(), proto.getNum());
                }
            }
        };
        NettyClientComponent.getInstance().connect("127.0.0.1", 6666,
                new ClientChannelInitializer(handler));

        // 模拟客户端发送数据
        int i = 0;
        while (i < 100) {
            SayHelloProto.Builder builder = SayHelloProto.newBuilder();
            builder.setNum(i);

            C2SPacket c2SPacket = new C2SPacket((short) 1);
            c2SPacket.setDestUserID(1001);
            c2SPacket.setBuilder(builder);
            NettyClientComponent.getInstance().send(c2SPacket);

            i++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
