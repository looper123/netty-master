package com.quark.netty.handler.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhenpengLu on 2018/6/25.
 */
public class TestSubscribeReqProto {

    //encode
    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    //decode
    private static SubscribeReqProto.SubscribeReq decode(byte[]  body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder build = SubscribeReqProto.SubscribeReq.newBuilder();
        build.setSubReqId(1);
        build.setUserName("lily");
        build.setProductName("book");
        build.setAddress("shenzhen hongshulin");
        return build.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req =createSubscribeReq();
        System.out.println("before encode:"+req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("after decode :"+req.toString());
        System.out.println("Assert eqaul:-->"+req2.equals(req));
    }
}
