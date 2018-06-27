package com.quark.netty.handler.msgPack;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by ZhenpengLu on 2018/6/21.
 * netty 时间服务器客户端
 */
public class TimeClient4 {

    public void connect(int port,String host) throws InterruptedException {
//        配置客户端NIO线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //解决粘包和拆包问题 （必须定义在自定义编码解码器之前 否则无法生效）
                            //LengthFieldBasedFrameDecoder 中的参数三 和  LengthFieldPrepender 参数对应
                            ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            ch.pipeline().addLast("framEncoder",new LengthFieldPrepender(2));
                            //msgpack编码器和解码器
                            ch.pipeline().addLast("msgpack decoder",new MsgPackDecoder());
                            ch.pipeline().addLast("msgpack encoder",new MsgPackEncoder());
                            ch.pipeline().addLast(new TimeClientHandler4(10));
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
        new TimeClient4().connect(port,"127.0.0.1");
    }
}
