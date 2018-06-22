package com.quark.netty.handler.decoder.fixedLength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by ZhenpengLu on 2018/5/31.
 * netty 自定义时间服务器服务端handler
 */
public class TimeServerHandler3 extends ChannelHandlerAdapter {

    //统计服务端收到客户端的消息总数
    private int counter;

    //通道读取
    @Override
    public void channelRead(ChannelHandlerContext ctx ,Object msg) throws UnsupportedEncodingException {
//        获取请求信息 (当timeServer中添加了解码器后就不需要像之前额外考虑处理读取半包问题，也不需要对请求消息进行编码)
//        ByteBuf buf = (ByteBuf)msg;
//        byte[]  req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req,"UTF-8").substring(0,req.length-System.getProperty("line.separator").length());
        String body = (String)msg;
        System.out.println("the time netty receive the order :"+body+": the count is :"+  ++counter);
        body +="$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.write(echo);
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
