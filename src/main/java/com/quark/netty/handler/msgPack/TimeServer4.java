package com.quark.netty.handler.msgPack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by ZhenpengLu on 2018/5/31.
 * netty 时间服务器服务端 使用netty的半包解码器解决 粘包/拆包的问题
 */
public class TimeServer4 {

    public void bind(int port) throws InterruptedException {
//        服务端的NIO线程组 一个用于接收客户端连接 一个用于socketchannel读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
//        netty启动NIO服务端的辅助启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            绑定启动类需要的参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .childHandler(new ChildChannelHand());
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //解决粘包和拆包问题 （必须定义在自定义编码解码器之前 否则无法生效）
                            //LengthFieldBasedFrameDecoder 中的参数三 和  LengthFieldPrepender 参数对应
                            socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            socketChannel.pipeline().addLast("framEncoder",new LengthFieldPrepender(2));
                            // msgpack编码器和解码器（自定义的编码解码器）
                            socketChannel.pipeline().addLast("msgpack decoder",new MsgPackDecoder());
                            socketChannel.pipeline().addLast("msgpack encoder",new MsgPackEncoder());
                            socketChannel.pipeline().addLast(new TimeServerHandler4());
                        }
                    });
//            绑定端口，同步等待成功
            ChannelFuture f = serverBootstrap.bind(port).sync();
//            等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
//            释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) throws InterruptedException {
       int port = 8080;
        if(args != null && args.length > 0 ){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new TimeServer4().bind(port);
    }
}





