package com.jsy.bankServices

/**
 * Created by lioa on 2015/4/23.
 */
interface IMessage {

    boolean  Valid(byte[] head)
    void getMessageBody(def body)
    /**
     * ִ�д���Ϣ(���İ󶨵Ĵ����¼�)
     * @return �������
     */
    boolean Execute()
    /**
     * ����Ϣ(����)���͸�������
     * @return ���ز������
     */
    boolean SendToServer()
    String getHead()
    String getMessageBody()
    def getMessageObj()
}