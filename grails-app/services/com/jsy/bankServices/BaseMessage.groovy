package com.jsy.bankServices

/**
 * 消息(报文)的基础类
 * Created by lioa on 2015/4/23.
 */
abstract class BaseMessage implements IMessage {
    HEAD head
    protected int code
    protected def _body

    boolean SendToServer() {

    }

    /**
     * 比较报文头是否相同
     * @param head
     * @return
     */
    boolean Valid(head) {

        return true
    }

    void getMessageBody(def body) {
        this._body = body
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
