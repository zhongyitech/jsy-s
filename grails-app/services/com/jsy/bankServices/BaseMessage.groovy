package com.jsy.bankServices

/**
 * ��Ϣ(����)�Ļ�����
 * Created by lioa on 2015/4/23.
 */
abstract class BaseMessage implements IMessage {
    protected byte[] head
    protected int code
    protected def _body

    boolean SendToServer() {

    }

    /**
     * �Ƚϱ���ͷ�Ƿ���ͬ
     * @param head
     * @return
     */
    boolean Valid(byte[] head) {
        if (this.head.size() != head.size())
            return false
        for (int i = 0; i < this.head.size(); i++) {
            if(this.head[i]!=head[i]) return false
        }
        return true
    }

    void getMessageBody(def body) {
        this._body=body
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
