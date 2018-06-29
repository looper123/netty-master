package com.quark.netty.customerProtocol;

import com.quark.netty.handler.marshalling.MarshallingCodecFactory;
import com.quark.netty.handler.msgPack.MsgPackDecoder;
import com.quark.netty.handler.msgPack.MsgPackEncoder;
import com.quark.netty.handler.msgPack.TimeClientHandler4;
import com.quark.netty.handler.protobuf.TimeClientHandler5;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZhenpengLu on 2018/6/27.
 */
public class NettyClient {

    //    定时task线程池 用以执行断线重连操作
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    NioEventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws InterruptedException {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
                            ch.pipeline().addLast("messageEncoder",new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("loginAuthHandler",new LoginAuthReqHandler());
                            ch.pipeline().addLast("heartBeatHandler",new HeartBeatReqHandler());
                        }
                    });
            ChannelFuture future = b.connect(
                    new InetSocketAddress("127.0.0.1",8080)
            ).sync();
            future.channel().closeFuture().sync();
        } finally {
//         所有资源释放完成后  清空资源 再次发起重连操作
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        connect(8080,"127.0.0.1");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect(8080,"127.0.0.1");
    }

}
