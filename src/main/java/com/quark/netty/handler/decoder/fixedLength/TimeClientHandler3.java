package com.quark.netty.handler.decoder.fixedLength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * 自定义netty 时间服务器客户端handler
 */
public class TimeClientHandler3 extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler3.class.getName());

    private int counter;

    private String  req  ;

    public TimeClientHandler3() {
        req = "QUERY TIME ORDER";
//        req = ("QUERY TIME ORDER"+"\n"+System.getProperty("line.separator")).getBytes();
    }

    //写入消息到缓冲区并且发送到socketchannel中
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf message = null;
//        发送一百条消息
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(req.getBytes()));
        }
    }

    //读取socketchannel中的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx ,Object msg) throws Exception{
//        在timeclient中添加了解码器后就不需要额外考虑半包和编码的问题了
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req,"UTF-8");
        String  body = (String)msg;
//        每接收到服务端的一条应答后就打印一次计数器
        System.out.println("body length :"+body.length()+"---the counter is :"+ ++counter);
    }

    //关闭通道并且通知到ChannelFuture
    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        logger.warning("Unexpected exception from downstream:"+cause.getMessage());
        ctx.close();
    }
}
