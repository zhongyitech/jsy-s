package com.jsy.bankServices

/**
 * 消息(报文)的基础类
 * Created by lioa on 2015/4/23.
 */
abstract class BaseMessage implements IMessage {
    protected byte[] head
    protected int code

    boolean SendToServer() {

    }

    boolean Valid(byte[] head) {

    }

    void getMessageBody(def body) {

    }

    boolean Execute() {

    }

    String getHead() {

    }

    String getMessageBody() {

    }

    def getMessageObj() {

    }
}
