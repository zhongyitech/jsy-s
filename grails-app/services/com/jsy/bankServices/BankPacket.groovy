package com.jsy.bankServices

import com.jsy.utility.MyException
import org.codehaus.groovy.grails.commons.GrailsArrayUtils
import java.nio.charset.Charset

/**
 * 通信报文头 + 报文体 + 文件附件头 + 附件内容报文体 + 报文签名数据
 * 通信报文头: 固定长度222 字节
 * 报文体:长度可变,整体长度定义在报文头中第5个域“接收报文长度”
 * 附件报文头为277位
 * Created by lioa on 2015/5/4.
 */
public class BankPacket {
    static final String BANK_INC_COMPANY_CODE = "00901079800000018000"
    static final String SUCCESS_CODE = "000000"
    public static Map RETURN_CODE = [
            "000000": [value: 0, "dest": "正常"],
            "GW3002": [value: 1, "dest": "银行内部系统通讯超时"],
            "EBLN00": [value: 2, "dest": "默认错误码"],
            "YQ9999": [value: 3, "dest": "银企平台程序故障"],
            "AFE004": [value: 4, "dest": "未知"],
            "E00008": [value: 5, "dest": "未知"],
    ]
    /**
     * 报文内容
     */
    byte[] _value
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
     * 根据字节数据创建一个报文结构体
     * @param xml 报文的内容(字节数组)
     */
    BankPacket(byte[] xml) {
        this._value = xml
        this.head = HEAD.CreateHead(xml)
        def dataLength = this.head.getFileDataLength()
        byte[] data = Arrays.copyOfRange(xml, this.head.PACKET_LENGTH, this.head.PACKET_LENGTH + dataLength.toInteger())
        //创建报文内容
        this.messageBody = MessageBody.createMessage(new String(data, this.head.getCharset()))
        int fileHeadStartIndex = HEAD.PACKET_LENGTH + dataLength
        if (xml.length > fileHeadStartIndex + FILEHEAD.PACKET_LENGTH) {
            this.filehead = FILEHEAD.CreateFILEHEAD(xml, head)
            int fileDataLength = this.filehead.getFileDataLength()
            //获取附件文件内容的编码格式
            def charset = this.filehead.getCharset()
            //创建报文内容
            this.fileMessageBody = MessageBody.createMessage(new String(Arrays.copyOfRange(xml, fileHeadStartIndex + FILEHEAD.PACKET_LENGTH, fileHeadStartIndex + FILEHEAD.PACKET_LENGTH + fileDataLength), charset))
        }
    }

    BankPacket(HEAD head, MessageBody body, FILEHEAD fhead, MessageBody fileBody) {
        this.head = head
        this.messageBody = body
        this.filehead = fhead
        this.fileMessageBody = fileBody
    }

    BankPacket(String xml) {
        BankPacket(xml.getBytes(HEAD.CHART_SET))
    }

    /**
     * 根据设置生成报文数据(用于请示发送)
     * @param character 字符格式
     * @return
     */
    public byte[] toBytes(Charset character = null) {
        if (character == null)
            character = Charset.forName(HEAD.CHART_SET)
        def result = new ArrayList<Byte>()
        def messageBytes = this.messageBody.getMessageBytes(character)
        def dataLength = messageBytes.length
        //设置报文头中的数据长度字段
        this.head.SetConfig("dataLength", dataLength)
        //添加报文头
        result.addAll(this.head.getHeadBytes())
        //添加报文体
        result.addAll(messageBytes)
        //添加附件文件头
        if (this.filehead != null && this.fileMessageBody != null) {
            def fMsgBytes = this.fileMessageBody.getMessageBytes(Charset.forName(this.filehead.getCharset()))
            this.filehead.SetConfig("dataLength", fMsgBytes.length)
            result.addAll(this.filehead.getHeadBytes())
            result.addAll(fMsgBytes)
        }
        return result
    }

    /**
     * 获取整个报文的字符表示数据
     * @param character
     * @return
     */
    public String toXmlString(Charset character = null) {
        return new String(_value, character == null ? Charset.forName(head.getCharset()) : character)
    }

    /**
     * 创建一个请上行报文
     * @param transactionNumber 交易代码
     * @param body 报文体
     * @param fileBody 附近件报文体
     * @return
     */
    static BankPacket CreateRequest(String transactionNumber, MessageBody body, MessageBody fileBody) {
        HEAD head = HEAD.GetDefaultRequestHead()
        //设置企业代码
        head.SetConfig("incCode", BANK_INC_COMPANY_CODE)
        //设置交易代码
        head.SetConfig("number", transactionNumber)
        //TODO:附近件报文头的生成规则未确定
        return new BankPacket(head, body, null, fileBody)
    }

    /**
     * 发送请求报文，成功之后调用委托方法,
     * @param closure 接收之后调用的方法
     * @return
     */
    public def SendPacket(Closure<BankPacket> closure) {
        Socket socket = getSocket();
        OutputStream out = socket.getOutputStream();
        InputStream din = socket.getInputStream();
        out.write(this.toBytes())
        out.flush();
        int lengthHeadLen = 222;
        int lengthHeadType = 0;
        int contentLength = 0;
        int off = 6; //todo:为什么要设置成 6 ?
        off = 0   //todo:确认之后要删除掉
        while (off < lengthHeadLen) {
            off = off + din.read(head._refValue, off, lengthHeadLen - off);
            if (off < 0) throw new EMPException("Socket was closed! while reading!");
        }
        def recode = head.GetConfig("returnCode")
        //验证返回码状态
        ValidationCode(recode)
        if (lengthHeadType == 0) {
            try {
                contentLength = head.getFileDataLength()
            } catch (Exception e) {
                throw new EMPException("获取报文头中的长度字段失败!");
            }
        }
        byte[] contentBuf = new byte[contentLength];
        off = 0;
        while (off < contentLength) {
            off = off + din.read(contentBuf, off, contentLength - off);
            if (off < 0) throw new EMPException("读取报文体数据出错,数据长度不够!")
        }
        socket.close()
        def data = GrailsArrayUtils.addAll(head.getHeadBytes(), contentBuf)
        closure.call(new BankPacket(data))
    }

    /**
     * 验证返回码状态,并根据返回码抛出异常信息
     * @param code
     */
    void ValidationCode(String code) {
        if (RETURN_CODE.containsKey(code)) {
            def scode = RETURN_CODE.get(code)
            if (code != SUCCESS_CODE && scode != null)
                throw new EMPException(code + ":" + scode.dest)
        }
    }
    /**
     * 返回一个配置好的Socket
     * @return
     */
    static Socket getSocket() {
        try {
            //TODO:从配置文件中读取前置机配置
            def socket = new Socket("127.0.0.1", 8885);
            socket.setSendBufferSize(4096);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(5000);
            socket.setKeepAlive(true);
            return socket
        }
        catch (ConnectException cex){
            throw new MyException("不能连接到银行前置服务器,请检测服务是否开启.")
        }
        catch (Exception ex) {
            throw ex
        }
    }
}
