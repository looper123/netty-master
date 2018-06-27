package com.quark.netty.handler.protobuf;

import com.quark.netty.handler.msgPack.MsgPackDecoder;
import com.quark.netty.handler.msgPack.MsgPackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Created by ZhenpengLu on 2018/5/31.
 * netty 时间服务器服务端 使用netty的半包解码器解决 粘包/拆包的问题
 */
public class TimeServer5 {

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
                            socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                            socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            socketChannel.pipeline().addLast(new ProtobufEncoder());
                            socketChannel.pipeline().addLast(new TimeServerHandler5());
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
        new TimeServer5().bind(port);
    }
}





