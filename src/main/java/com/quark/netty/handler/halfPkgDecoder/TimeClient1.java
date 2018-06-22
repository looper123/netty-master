package com.quark.netty.handler.halfPkgDecoder;

import com.quark.netty.handler.simple.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * netty 时间服务器客户端
 */
public class TimeClient1 {

    public void connect(int port,String host) throws InterruptedException {
//        配置客户端NIO线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            新增解码器
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler1());
                        }
                    });
//        发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();
//        等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
//            释放NIO线程组
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if(args != null && args.length >0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new TimeClient1().connect(port,"127.0.0.1");
    }
}
