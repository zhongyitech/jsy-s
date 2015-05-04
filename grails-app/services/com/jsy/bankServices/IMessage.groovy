package com.jsy.bankServices

/**
 * Created by lioa on 2015/4/23.
 */
interface IMessage {

    boolean  Valid(byte[] head)
    void getMessageBody(def body)
    /**
     * 执行此消息(报文绑定的处理事件)
     * @return 操作结果
     */
    boolean Execute()
    /**
     * 将消息(报文)发送给服务器
     * @return 返回操作结果
     */
    boolean SendToServer()
    String getHead()
    String getMessageBody()
    def getMessageObj()
}