package com.quark.netty.handler.protobuf;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by ZhenpengLu on 2018/5/31.
 * netty 自定义时间服务器服务端handler
 */
public class TimeServerHandler5 extends ChannelHandlerAdapter {

    //通道读取 SubscribeReq
    @Override
    public void channelRead(ChannelHandlerContext ctx ,Object msg) throws UnsupportedEncodingException {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq)msg;
        if("looper".equalsIgnoreCase(req.getUserName())){
            System.out.println("service accept client subscribe req:{"+req.toString()+"}");
            ctx.writeAndFlush(resp(req.getSubReqId()));
        }
    }

    //服务端给客户端的响应 SubscribeResp
    private SubscribeRespProto.SubscribeResp resp(int subReqId) {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqId(subReqId);
        builder.setRespCode(0);
        builder.setDesc("netty book order succeed, 3 days later,sent to the designated address");
        return builder.build();
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
