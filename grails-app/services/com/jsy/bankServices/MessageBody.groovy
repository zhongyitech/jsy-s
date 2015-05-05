package com.jsy.bankServices

import java.nio.charset.Charset

/**
 * ������
 * XML ��ʽ���ı�
 * Created by lioa on 2015/5/4.
 */
class MessageBody {

    private String _xml

    public static MessageBody createMessage(String xml) {
        return new MessageBody(xml)
    }

    MessageBody(String  _xml) {
        this._xml = _xml
    }

    public def getResult() {

    }

    public def getPage() {

    }

    public  byte [] getMessageBytes(Charset charset){
        return  _xml.getBytes(charset)
    }

}
