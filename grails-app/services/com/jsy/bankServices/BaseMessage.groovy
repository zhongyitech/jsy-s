package com.jsy.bankServices

/**
 * ��Ϣ(����)�Ļ�����
 * Created by lioa on 2015/4/23.
 */
abstract class BaseMessage implements IMessage {
    HEAD head
    protected int code
    protected def _body

    boolean SendToServer() {

    }

    /**
     * �Ƚϱ���ͷ�Ƿ���ͬ
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
