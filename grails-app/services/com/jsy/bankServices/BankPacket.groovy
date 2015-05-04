package com.jsy.bankServices

import java.nio.charset.Charset

/**
 * 通信报文头 + 报文体 + 文件附件头 + 附件内容报文体 + 报文签名数据
 * 通信报文头: 固定长度222 字节
 * 报文体:长度可变,整体长度定义在报文头中第5个域“接收报文长度”
 * 附件报文头为277位
 * Created by lioa on 2015/5/4.
 */
public class BankPacket {
    /**
     * 报文头
     */
    HEAD head
    /**
     * 报文体
     */
    MessageBody messageBody
    /**
     * 附件报文头
     */
    FILEHEAD filehead

    MessageBody fileMessageBody
    /**
     *
     * @param xml 报文的内容
     */
    BankPacket(byte[] xml) {
        this.head = HEAD.CreateHead(xml)
        def dataLength = this.head.getFileDataLength()
        byte[] data = Arrays.copyOfRange(xml, this.head.PACKET_LENGTH, this.head.PACKET_LENGTH + dataLength.toInteger())
//        byte[] data = xml[this.head.PACKET_LENGTH - 1,this.head.PACKET_LENGTH -1+ dataLength]
        this.messageBody = MessageBody.createMessage(new String(data,this.head.getCharset()))
        int fileHeadStartIndex = HEAD.PACKET_LENGTH + dataLength
        if (xml.length > fileHeadStartIndex + FILEHEAD.PACKET_LENGTH) {
            byte[] fileHeadBytes = Arrays.copyOfRange(xml, fileHeadStartIndex, fileHeadStartIndex + FILEHEAD.PACKET_LENGTH)
            this.filehead = FILEHEAD.CreateFILEHEAD(fileHeadBytes)
            int fileDataLength = this.filehead.getFileDataLength()
            //获取附件文件内容的编码格式
            def charset =this.filehead.getCharset()
            this.fileMessageBody = MessageBody.createMessage(new String(Arrays.copyOfRange(xml, fileHeadStartIndex + FILEHEAD.PACKET_LENGTH, fileHeadStartIndex + FILEHEAD.PACKET_LENGTH + fileDataLength), charset))
        }
    }

    BankPacket(String xml) {
    }

    public byte[] toBytes(Charset character = null) {
        if (character == null)
            character = Charset.forName(HEAD.CHART_SET)
        def result = new ArrayList<Byte>()

        def messageBtyes = this.messageBody.getMessageBytes(character)
        def dataLength=messageBtyes.length
        this.head.SetConfig(HEAD.getPackConfig().dataLength,"00")
        result.addAll(this.head.getHeadBytes())
        result.addAll(this.messageBody.getMessageBytes())
        if (this.filehead != null) {
            result.addAll(this.filehead.getHeadBytes(character))
        }
        return result
    }

    public String toXmlString(Charset character = null) {
        return new String(toBytes(character), character == null ? Charset.forName(HEAD.CHART_SET) : character)
    }
}
