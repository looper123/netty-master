package com.quark.netty.handler.withoutDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by ZhenpengLu on 2018/5/31.
 * netty 自定义时间服务器服务端handler
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    //通道读取
    @Override
    public void channelRead(ChannelHandlerContext ctx ,Object msg) throws UnsupportedEncodingException {
//        获取请求信息
        ByteBuf buf = (ByteBuf)msg;
        byte[]  req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("the time netty receive the order :"+body);
//         如果请求信息包含了QUERY TIME ORDER 则创建应答信息
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?
                new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    //通道读取完毕 把消息发送队列中的消息写入到socketchannel中发送给对方
    //为什么netty的wirte方法不直接把消息写入到socketchannel中，而是调用了flush方法后才写入？？
    //原因：为了防止频繁的唤醒selector进行消息发送，netty的write方法并不直接把消息写入到socketchannel中，而是先发送到缓冲数组中，
    //再通过调用flush方法，将缓冲区的消息全部写入到socketchannel中
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    //异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        ctx.close();
    }




}
