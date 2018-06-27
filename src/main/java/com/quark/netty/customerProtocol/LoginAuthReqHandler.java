package com.quark.netty.customerProtocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZhenpengLu on 2018/6/27.
 * 客户端登录handler
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getValue());
        message.setHeader(header);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;
//        如果握手应答消息 需要判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()) {
            byte loginResult = Byte.parseByte((String) message.getBody());
            if (loginResult != (byte) 0) {
//          握手失败 关闭连接
                ctx.close();
            } else {
                System.out.println("login is ok :" + message);
                ctx.fireChannelRead(msg);
            }
        }
    }
}