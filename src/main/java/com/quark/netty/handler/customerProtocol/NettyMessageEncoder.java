package com.quark.netty.handler.customerProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhenpengLu on 2018/6/26.
 * netty消息编码类
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    private CustomerMarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() {
        this.marshallingEncoder = marshallingEncoder;
    }

    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
         if(msg == null|| msg.getHeader() ==null )
             throw new Exception("the encode message is null");
         ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt((msg.getHeader().getCrcCode()));
        sendBuf.writeInt((msg.getHeader().getLength()));
        sendBuf.writeLong((msg.getHeader().getSessionID()));
        sendBuf.writeByte((msg.getHeader().getType()));
        sendBuf.writeByte((msg.getHeader().getPriority()));
        sendBuf.writeInt((msg.getHeader().getAttachment().size()));
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for(Map.Entry<String,Object> param:msg.getHeader().getAttachment().entrySet()){
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(ctx,value,sendBuf);
        }
        key = null;
        keyArray =null;
        value = null;
        if(msg.getBody() != null){
            marshallingEncoder.encode(ctx,msg.getBody(),sendBuf);
        }else {
            sendBuf.writeInt(0);
            sendBuf.setInt(4,sendBuf.readableBytes());
        }
    }

}
