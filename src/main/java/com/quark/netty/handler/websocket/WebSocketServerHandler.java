package com.quark.netty.handler.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZhenpengLu on 2018/6/26.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
//        传统http接入
        if(msg instanceof FullHttpRequest){
            handleRequest(ctx,(FullHttpRequest)msg);
        }
//        Websocket 接入
        else if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx,(WebSocketFrame)msg);
        }
    }

//    处理websocket协议
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) throws UnsupportedOperationException {
//        是否是关闭链路的命令
        if(msg instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)msg.retain());
            return ;
        }
//        判断是否是ping消息
        if(msg instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
            return ;
        }
//        本例程仅支持文本消息，不支持二进制消息
        if(!(msg instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame types not supported",msg.getClass().getName()));
        }
//        返回应答消息
        String request = ((TextWebSocketFrame)msg).text();
        if(logger.isLoggable((Level.FINE))){
            logger.fine(String.format("%s reveive %s",ctx.channel()));
        }
        ctx.channel().write(new TextWebSocketFrame(request+",欢迎使用netty websocket服务，now time is："+new Date().toString()));
    }

//      处理http协议
    private void handleRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        //解码失败 返回异常
        if(msg.decoderResult().isFailure() || (!"websocket".equals(msg.headers().get("Upgrade")))){
            sendHttpResponse(ctx,msg,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
            return ;
        }
//        构造握手响应
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
        handshaker = wsFactory.newHandshaker(msg);
        if(handshaker  == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),msg);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest msg, DefaultFullHttpResponse response) {
//        对客户端的响应
        if(response.status().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
//            setContentLength(response,response.content().readableBytes());
        }
//       如果不是keep-alive 关闭连接
//        ChannelFuture f = ctx.channel().writeAndFlush(response);
//        if(!isKeepAlive(msg) || response.status().code() != 200){
//            f.addListener(ChannelFutureListener.CLOSE);
//        }
    }

    private boolean isKeepAlive(FullHttpRequest request) {
        return false;
    }

    private void setContentLength(DefaultFullHttpResponse response, int i) {
    }

    @Override
    public  void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        cause.printStackTrace();
        ctx.close();
    }

}
