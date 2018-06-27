package com.quark.netty.handler.protobuf;

import com.quark.netty.handler.entity.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * 自定义netty 时间服务器客户端handler
 */
public class TimeClientHandler5 extends ChannelHandlerAdapter {


    //写入消息到缓冲区并且发送到socketchannel中
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subReq(int i) {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(i);
        builder.setUserName("looper");
        builder.setProductName("netty book for ProtoBuf");
        builder.setAddress("shanghai disney");
        return builder.build();
    }


    //接收服务端的响应 SubscribeResp
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeRespProto.SubscribeResp resp = (SubscribeRespProto.SubscribeResp)msg;
        System.out.println("receive server response" + resp);
        ctx.write(resp);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.flush();
    }
}
