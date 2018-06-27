package com.quark.netty.handler.protobuf;

import com.quark.netty.handler.msgPack.MsgPackDecoder;
import com.quark.netty.handler.msgPack.MsgPackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * netty 时间服务器客户端
 */
public class TimeClient5 {

    public void connect(int port,String host) throws InterruptedException {
//        配置客户端NIO线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            netty对protobuf提供了三种处理半包的方法(这里只使用了一种)
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());//split bytebuf  (remove extra bytes  what  added  previously )
                            ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));//create a new instance
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());// prepend  bytebuf (add extra bytes)
                            ch.pipeline().addLast(new ProtobufEncoder()); //encode  protobuf type to  bytebuf type
                            ch.pipeline().addLast(new TimeClientHandler5());
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
        new TimeClient5().connect(port,"127.0.0.1");
    }
}
