package cn.arry.netty.codec;

import cn.arry.netty.codec.decoder.ClientMessageDecoder;
import cn.arry.netty.codec.decoder.ServerMessageDecoder;
import cn.arry.netty.codec.encoder.ClientMessageEncoder;
import cn.arry.netty.codec.encoder.ServerMessageEncoder;

public class MessageCodecFactory {
    public static ServerMessageEncoder getSEncoder() {
        return new ServerMessageEncoder();
    }

    public static ServerMessageDecoder getSDecoder() {
        return new ServerMessageDecoder();
    }

    public static ClientMessageEncoder getCEncoder() {
        return new ClientMessageEncoder();
    }

    public static ClientMessageDecoder getCDecoder() {
        return new ClientMessageDecoder();
    }
}
