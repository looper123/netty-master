package com.quark.netty.customerProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * Created by ZhenpengLu on 2018/6/26.
 * MarshallingDecoder 的子类
 */
public class CustomerMarshallingDecoder extends MarshallingDecoder {

    public CustomerMarshallingDecoder(UnmarshallerProvider provider) {
        super(provider);
    }

    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
       return  super.decode(ctx,in);
    }
}
