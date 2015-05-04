package com.jsy.bankServices

import grails.plugin.springsecurity.InterceptedUrl

import java.nio.charset.Charset

/**
 * 通讯报文头 222字节
 * 报文组成: 通信报文头 + 报文体 + 文件附件头 + 附件内容报文体 + 报文签名数据
 * Created by lioa on 2015/4/24.
 */
class HEAD {
    /**
     * 报文数据的长度
     */
    public static final int PACKET_LENGTH = 222
    public static final String CHART_SET = "GBK"
    //空值填充值
    public static final byte CHART_NULL = 0x20
    public static final byte OZER = 0x00

    public static final Map packConfig = [
            charset               : [s: 6, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "报文编码"],
            ptype                 : [s: 0, l: 6, dest: "报文类别和目标系统编码"],
            dataLength            : [s: 30, l: 10, dest: "数据长度"],
            number                : [s: 40, l: 6, dest: "交易码"],
            serverType            : [s: 51, l: 2, dest: "服务类型", data: ["01": "request", "02": "response"]],
            requestTransactionCode: [s: 67, l: 20, dest: "请求方交易流水号,多次交易请求包含"],
            returnCode            : [s: 87, l: 6, dest: "返回码"],
            returnDescription     : [s: 93, l: 100, dest: "返回描述"],
            transactionDate       : [s: 53, l: 8, dest: "交易日期"],
            transactionTime       : [s: 61, l: 6, dest: "交易时间"],
            requestTick           : [s: 194, l: 3, dest: "请求次数,流水号要和第一次保持一致"]
    ]
    /**
     * 报文数据 以字符串方式存储
     */
    private String _value

    /**
     * 返回内部数据报文的数据,经过解码的数据
     * @return
     */
    public String getPacketValue() {
        return _value
    }

    public static Map getPackConfig() {
        return packConfig
    }
    /**
     * 创建一个报文头
     * @param _value 字符串格式的报文头数据
     */
    HEAD(String _value) {
        if (_value == null || _value.getBytes(CHART_SET).length != PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        this._value = _value
    }
    /**
     * 创建一个报文头
     * @param _value 字节格式的数据
     */
    HEAD(byte[] _value) {
        if (_value == null || _value.length < PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        Charset charset = Charset.forName(CHART_SET)
        def headBytes = Arrays.copyOfRange(_value, 0, PACKET_LENGTH)
        this._value = new String(headBytes, charset)
    }
    /**
     * 获取报文的字节格式数据
     * @return 报文头的字节数据
     */
    public byte[] getHeadBytes(Charset charset = null) {
        if (charset == null) {
            charset = Charset.forName(CHART_SET)
        }
        def result = _value.getBytes(charset)
        //对空字符串进行替换
        result.each {
            if (it.byteValue() == OZER)
                it = CHART_NULL
        }
        return result
    }

    //数据内容的长度
    public Long getFileDataLength() { return Long.parseLong(GetConfig(getPackConfig().dataLength)) }

    //返回码
    public String getReturnCode() { return GetConfig(getPackConfig().returnCode) }

    public String getReturnDescription() { return GetConfig(getPackConfig().returnDescription) }

    public String getNumber() { return GetConfig(getPackConfig().number) }

    public Charset getCharset() { return Charset.forName(GetConfig(getPackConfig().charset)) }

    //通用的方法
    static CreateHead(byte[] head) {
        return new HEAD(head)
    }

    static CreateHead(String headData) {
        return new HEAD(headData)
    }
    /**
     * 获取某一信息
     * @param map 参数的名称
     * @return 参数的值
     */
    public String GetConfig(Map map) {
        def result = _value.substring(map.s, map.s + map.l)
        if (map.data) {
            println(map.dest + ":" + map.data[result] + " (" + result + ")")
            return map.data[result]
        } else {
            println(map.dest + ":" + result)
        }
        return result
    }

    /**
     * 设置报文头的参数
     * @param location 参数的名称
     * @param value
     */
    public <T> void SetConfig(Map location, T value) {
        String newValue = ""
        //数字前补0
        if (value.class.name == Integer.class.name) {
            newValue = String.format("%0" + location.l + "d", value).replace(" ", "0");
        }
        //字符后补空格
        if (value.class.name == String.class.name) {
            if ((value as String).length() > location.l) {
                throw new Exception("要设置的值不符合规则,长度应该为:" + location.l)
            }
            newValue = value as String
        }
        def old = GetConfig(location)
        _value = _value.replace(old, newValue)
    }

    public static void main(String[] args) {
        def xml = "A0010101010010207990000123100000000004944004  12345012010081115421620100811153400      999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001<?xml version=\"1.0\" encoding=\"GB2312\"?><Result><ThirdVoucher>20100811153416</ThirdVoucher><CcyCode>RMB</CcyCode><OutAcctNo>11000097408701</OutAcctNo><OutAcctName>ebt</OutAcctName><OutAcctAddr/><InAcctBankNode/><InAcctRecCode/><InAcctNo>11000098571501</InAcctNo><InAcctName>EBANK</InAcctName><InAcctBankName>anything</InAcctBankName><TranAmount>000.01</TranAmount><AmountCode/><UseEx/><UnionFlag>1</UnionFlag><SysFlag>2</SysFlag><AddrFlag>1</AddrFlag><RealFlag>2</RealFlag><MainAcctNo/></Result>123.txt                                                                                                                                                                                                                                         0200011111111111100000000000000000004ABCD"
        def packet = new BankPacket(xml.getBytes(Charset.forName("GBK")))
        println(packet.head.getReturnCode())
        packet.toBytes()
        print(packet.toXmlString())
    }
}
