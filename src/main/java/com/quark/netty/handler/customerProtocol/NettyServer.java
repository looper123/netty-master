package com.quark.netty.handler.customerProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by ZhenpengLu on 2018/6/27.
 */
public class NettyServer {

    public void bind()  throws Exception{
//        服务端的NIO线程组 一个用于接收客户端连接 一个用于socketchannel读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//        try{
//        netty启动NIO服务端的辅助启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
//            绑定启动类需要的参数
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
//                内核为此套接口排队的最大连接数 （已连接队列 （完成了三次握手）+ 未连接队列（三次握手未全部完成） ）
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
                        socketChannel.pipeline().addLast("messageEncoder",new NettyMessageEncoder());
                        socketChannel.pipeline().addLast(new ReadTimeoutHandler(50));
                        socketChannel.pipeline().addLast(new LoginAuthRespHandler());
                        socketChannel.pipeline().addLast("heartBeathandler",new HeartBeatRespHandler());
                    }
                });
//            绑定端口，同步等待成功
        ChannelFuture f = serverBootstrap.bind("127.0.0.1",8080).sync();
        System.out.println("netty server start ok——>127.0.0.1:8080");
//            等待服务端监听端口关闭
            f.channel().closeFuture().sync();
//        } finally {
//            释放线程池资源
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }

}
