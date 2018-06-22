package com.quark.netty.handler.withoutDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * 自定义netty 时间服务器客户端handler
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

    private  final ByteBuf firstMessage;

    public TimeClientHandler() {
//        this.firstMessage = firstMessage;
        byte[]  req ="QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    //写入消息到缓冲区并且发送到socketchannel中
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(firstMessage);
    }

    //读取socketchannel中的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx ,Object msg) throws Exception{
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("now is :"+body);
    }

    //关闭通道并且通知到ChannelFuture
    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        logger.warning("Unexpected exception from downstream:"+cause.getMessage());
        ctx.close();
    }
}
