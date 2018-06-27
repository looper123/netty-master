package com.quark.netty.handler.msgPack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by ZhenpengLu on 2018/6/25.
 * messagePack 编码器
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object>{
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack msgpack = new MessagePack();
        //把对象编码成byte数组
        byte[] raw = msgpack.write(msg);
        //写入到bytebuf中
        out.writeBytes(raw);
    }
}
