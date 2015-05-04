package com.jsy.bankServices

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
    public final int PACKET_LENGTH = 222
    public final String CHART_SET = "GBK"
    static final Map packConfig = [
            charset               : [s: 6, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "报文编码"],
            ptype                 : [s: 0, l: 6, dest: "报文类别和目标系统编码"],
            dataLength            : [s: 30, l: 10, dest: "数据长度"],
            number                : [s: 40, l: 6, dest: "交易码"],
            serverType            : [s: 51, l: 2, dest: "服务类型", data: [01: "request", 02: "response"]],
            requestTransactionCode: [s: 67, l: 2, dest: "请求方交易流水号,多次交易请求包含"],
            transactionDate       : [s: 53, l: 8, dest: "交易日期"],
            transactionTime       : [s: 61, l: 6, dest: "交易时间"]
    ]
    /**
     * 报文数据 以字符串方式存储
     */
    private String _value

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
        if (_value == null || _value.length != PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        Charset charset = Charset.forName(CHART_SET)
        this._value = new String(_value, charset)
    }
    /**
     * 获取报文的字节格式数据
     * @return
     */
    public byte[] getHeadBytes() {
        return _value.getBytes(CHART_SET)
    }

    //通用的方法
    static getHead(byte[] head) {
        return new HEAD(head)
    }

    static getHead(String headData) {
        return HEAD(headData)
    }
    /**
     * 获取某一信息
     * @param map 参数的名称
     * @return 参数的值
     */
    public String GetConfig(Map map) {
        def result = _value.substring(map.s, map.s + map.l)
        if (map.data) {
            print(map.dest + ":" + map.data[result] + " (" + result + ")")
        } else {
            print(map.dest + ":" + result)
        }
        return result
    }

    public static void Test() {
        def head = new HEAD("A0010101010010107990000999900000000005114004  12345012010081115421620100811153400      999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
        def charset = head.GetConfig(getPackConfig().charset)
        head.GetConfig(getPackConfig().ptype)
        head.GetConfig(getPackConfig().inccode)
        def head2 = new HEAD(head.getHeadBytes())
    }
}
