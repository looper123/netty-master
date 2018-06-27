package com.quark.netty.handler.entity;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by ZhenpengLu on 2018/6/25.
 */
@Message  //把实体标识为msgpack能识别的类型
public class UserInfo implements Serializable {

    private static  final long serialVersionUID = 1L;

    private String userName;

    private int userID;

    private int age;

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
