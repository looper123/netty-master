package com.quark.netty.handler.msgPack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by ZhenpengLu on 2018/6/25.
 * messagePack 解码器
 */
public class MsgPackDecoder  extends MessageToMessageDecoder<ByteBuf>{
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        //获取byte数组
        final int length  = msg.readableBytes();
        array = new byte[length];
        //把获取到的数组放入array数组中
        msg.getBytes(msg.readerIndex(),array,0,length);
        MessagePack messagePack = new MessagePack();
        //把解码后的对象加入到解码列表中
        out.add(messagePack.read(array));
    }
}
