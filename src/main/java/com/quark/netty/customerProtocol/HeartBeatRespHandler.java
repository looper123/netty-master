package com.quark.netty.customerProtocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhenpengLu on 2018/6/27.
 * 服务端心跳handler
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;
//        如果接受到来自客户端的信条信息 就给出心跳响应
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.getValue()) {
            System.out.println("receive client heart bet message:——>" + message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("send heart beat response message to client: ——>" + heartBeat);
            ctx.writeAndFlush(heartBeat);
//            没有接收到来自客户端的心跳 则放心
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.getValue());
        message.setHeader(header);
        return message;

    }
}
