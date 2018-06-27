package com.quark.netty.handler.msgPack;

import com.quark.netty.handler.entity.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * 自定义netty 时间服务器客户端handler
 */
public class TimeClientHandler4 extends ChannelHandlerAdapter {

    private final int sendNumber;

    public TimeClientHandler4(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    //写入消息到缓冲区并且发送到socketchannel中
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        UserInfo[] infos = UserInfo();
        for (UserInfo info : infos
                ) {
            ctx.writeAndFlush(info);
        }
//        ctx.flush();
    }

    private UserInfo[] UserInfo() {
        UserInfo[] userInfos = new UserInfo[sendNumber];
        UserInfo userInfo = null;
        for (int i = 0; i < sendNumber; i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setUserName("ABCDEF---->" + i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        UserInfo userInfo = (UserInfo)msg;
        System.out.println("Client receive the msgpack message" + msg);
        ctx.write(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.flush();
    }
}
