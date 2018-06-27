package com.quark.netty.customerProtocol;



/**
 * Created by ZhenpengLu on 2018/6/26.
 * netty协议实体类
 */
public final class NettyMessage {

    private Header header;

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                '}';
    }
}
