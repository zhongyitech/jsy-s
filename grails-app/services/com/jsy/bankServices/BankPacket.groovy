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
//            byte[] fileHeadBytes = Arrays.copyOfRange(xml, fileHeadStartIndex, fileHeadStartIndex + FILEHEAD.PACKET_LENGTH)
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
        BankPacket(xml.getBytes("GBK"))
    }

    /**
     * 根据设置生成报文数据(用于请示发送)
     * @param character
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
        result.addAll(this.messageBody.getMessageBytes(character))
        //添加附件文件头
        if (this.filehead != null) {
            result.addAll(this.filehead.getHeadBytes())
            //添加附件报文本
            if (this.fileMessageBody) {
                result.addAll(this.fileMessageBody.getMessageBytes(character))
            }
        }
        return result
    }

    public String toXmlString(Charset character = null) {
        return new String(_value, character == null ? Charset.forName(HEAD.CHART_SET) : character)
    }

    //工具方法
    static BankPacket CreateRequest(String transactionNumder, MessageBody body, MessageBody fileBody) {

        HEAD head = HEAD.GetDefaultRequestHead()
        //设置企业代码
        head.SetConfig("incCode", "00901079800000018000")
        //设置交易代码
        head.SetConfig("number", transactionNumder)

        return new BankPacket(head, body, null, fileBody)
    }

    //发送报文加并处理回复
    public def SendPacket(Closure<BankPacket> closure) {

        if (this.head.getNumber() == "4001") {
            def result = new BankPacket("A0010101010010107990000999900000000003994001       02201008091710282010080981026055    000000:交易受理成功                                                                                       00000                       0<?xml version=\"1.0\" encoding=\"GBK\" ?><Result><Account>11002873390701</Account><CcyCode>RMB</CcyCode><CcyType></CcyType><AccountName>優比速包裹運送（  Ｖ｜  ）有 分公司</AccountName><Balance>130802332974.55</Balance><TotalAmount>130802332974.55</TotalAmount><AccountType></AccountType><AccountStatus></AccountStatus><BankName></BankName><LastBalance></LastBalance><HoldBalance></HoldBalance></Result>"
                    .getBytes("GBK"))
            return closure.call(result)
        }
        if (this.head.getNumber() == "4013") {
            def result = new BankPacket("A0010101010010107990000999900000000003994001       02201008091710282010080981026055    000000:交易受理成功                                                                                       00000                       0<?xml version=\"1.0\" encoding=\"GBK\" ?><Result><Account>11002873390701</Account><CcyCode>RMB</CcyCode><CcyType></CcyType><AccountName>優比速包裹運送（  Ｖ｜  ）有 分公司</AccountName><Balance>130802332974.55</Balance><TotalAmount>130802332974.55</TotalAmount><AccountType></AccountType><AccountStatus></AccountStatus><BankName></BankName><LastBalance></LastBalance><HoldBalance></HoldBalance></Result>"
                    .getBytes("GBK"))
            result.messageBody=new MessageBody("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                    "<Result>" +
                    "<AcctNo></AcctNo>" +
                    "<CcyCode></CcyCode>" +
                    "<EndFlag></EndFlag>" +
                    "<Reserve></Reserve>" +
                    "<PageRecCount></PageRecCount>" +
                    "<list>" +
                    "<AcctDate></AcctDate>" +
                    "<TxTime></TxTime>" +
                    "<HostTrace></HostTrace>" +
                    "<OutNode></OutNode>" +
                    "<OutBankNo></OutBankNo>" +
                    "<OutBankName></OutBankName>" +
                    "<OutAcctNo></OutAcctNo>" +
                    "<OutAcctName></OutAcctName>" +
                    "<CcyCode></CcyCode>" +
                    "<TranAmount></TranAmount>" +
                    "<InNode></InNode>" +
                    "<InBankNo></InBankNo>" +
                    "<InBankName></InBankName>" +
                    "<InAcctNo></InAcctNo>" +
                    "<InAcctName></InAcctName>" +
                    "<DcFlag></DcFlag>" +
                    "<AbstractStr></AbstractStr>" +
                    "<VoucherNo></VoucherNo>" +
                    "<TranFee></TranFee>" +
                    "<PostFee></PostFee>" +
                    "<AcctBalance></AcctBalance>" +
                    "<Purpose ></Purpose>" +
                    "</list>" +
                    "<list>" +
                    "<AcctDate></AcctDate>" +
                    "<TxTime></TxTime>" +
                    "<HostTrace></HostTrace>" +
                    "<OutNode></OutNode>" +
                    "<OutBankNo></OutBankNo>" +
                    "<OutBankName></OutBankName>" +
                    "<OutAcctNo></OutAcctNo>" +
                    "<OutAcctName></OutAcctName>" +
                    "<CcyCode></CcyCode>" +
                    "<TranAmount></TranAmount>" +
                    "<InNode></InNode>" +
                    "<InBankNo></InBankNo>" +
                    "<InBankName></InBankName>" +
                    "<InAcctNo></InAcctNo>" +
                    "<InAcctName></InAcctName>" +
                    "<DcFlag></DcFlag>" +
                    "<AbstractStr></AbstractStr>" +
                    "<VoucherNo></VoucherNo>" +
                    "<TranFee></TranFee>" +
                    "<PostFee></PostFee>" +
                    "<AcctBalance></AcctBalance>" +
                    "<Purpose ></Purpose>" +
                    "</list>" +
                    "<list>" +
                    "<AcctDate></AcctDate>" +
                    "<TxTime></TxTime>" +
                    "<HostTrace></HostTrace>" +
                    "<OutNode></OutNode>" +
                    "<OutBankNo></OutBankNo>" +
                    "<OutBankName></OutBankName>" +
                    "<OutAcctNo></OutAcctNo>" +
                    "<OutAcctName></OutAcctName>" +
                    "<CcyCode></CcyCode>" +
                    "<TranAmount></TranAmount>" +
                    "<InNode></InNode>" +
                    "<InBankNo></InBankNo>" +
                    "<InBankName></InBankName>" +
                    "<InAcctNo></InAcctNo>" +
                    "<InAcctName></InAcctName>" +
                    "<DcFlag></DcFlag>" +
                    "<AbstractStr></AbstractStr>" +
                    "<VoucherNo></VoucherNo>" +
                    "<TranFee></TranFee>" +
                    "<PostFee></PostFee>" +
                    "<AcctBalance></AcctBalance>" +
                    "<Purpose ></Purpose>" +
                    "</list>" +
                    "</Result>")
            return closure.call(result)
        }
        return null

        //test
        Socket s = new Socket("testebank.sdb.com.cn", 462);
        s.setSendBufferSize(4096);
        s.setTcpNoDelay(true);
        s.setSoTimeout(5000);
        s.setKeepAlive(true);
        OutputStream out = s.getOutputStream();
        InputStream din = s.getInputStream();

        //准备报文src
        out.write("A0010102010090103000000004100000000001104001       0120100809171028    2010080981026055                                                                                                          00000                       0<?xml version=\"1.0\" encoding=\"UTF-8\"?><Result><Account>11002873390701</Account><CcyCode>RMB</CcyCode></Result>".getBytes("UTF-8"));
        out.flush();
        int lengthHeadLen = 222;
        int lengthHeadType = 0;
        int lengthBodyLenStartposi = 30;
        int lengthBodyLen = 10;
        int contentLength = 0;

        byte[] lenHeadBuf = new byte[lengthHeadLen]
        System.arraycopy(head, 0, lenHeadBuf, 0, head.length);
        int off = 6;
        while (off < lengthHeadLen) {
            off = off + din.read(lenHeadBuf, off, lengthHeadLen - off);
            if (off < 0) {
//                errMsg = "ERROR:while reading 222 head.";
//                return errMsg.getBytes();
                throw new EMPException("Socket was closed! while reading!");
            }
        }
//        byteout.write(lenHeadBuf);
        //获取报文头中的长度字段
        if (lengthHeadType == 0) {
            try {
                contentLength = Integer.parseInt(new String(lenHeadBuf,
                        lengthBodyLenStartposi, lengthBodyLen).trim());
            } catch (Exception e) {
//                Trace.logError(Trace.COMPONENT_TCPIP,
//                        "获取报文头中的长度字段失败--->lenHeadBuf=" + new String(lenHeadBuf));
                throw new EMPException("获取报文头中的长度字段失败!");
            }
        }
        //read msg content.
        byte[] contentBuf = new byte[contentLength];
        off = 0;
        while (off < contentLength) {
            off = off + din.read(contentBuf, off, contentLength - off);
            if (off < 0) {
//                errMsg = "ERROR:while reading length-[" + contentLength + "] body.";
//                return errMsg.getBytes();
                throw new EMPException("Socket was closed! while reading!");
            }
        }
        closure.call(new BankPacket(contentBuf))
    }
}
