package com.quark.netty.handler.customerProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 * Created by ZhenpengLu on 2018/6/26.
 * MarshallingEncoder 子类
 */
public class CustomerMarshallingEncoder extends MarshallingEncoder {

    /**
     * Creates a new encoder.
     *
     * @param provider the {@link MarshallerProvider} to use
     */
    public CustomerMarshallingEncoder(MarshallerProvider provider) throws Exception {
        super(provider);

    }

    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            super.encode(ctx, msg, out);
    }


}
