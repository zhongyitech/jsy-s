package com.jsy.bankServices

import com.jsy.bankPacket.Pack4004
import com.jsy.utility.UtilityString
import org.h2.message.Trace

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
    public static String CHART_SET = "GBK"
    //空值填充值
    public static final byte CHART_NULL = 0x20
    public static final byte OZER = 0x00

    public static final Map packConfig = [
            charset               : [s: 6, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "报文编码"],
            ptype                 : [s: 0, l: 6, dest: "报文类别和目标系统编码"],
            incCode               : [s: 10, l: 20, dest: "企业银企直连标准代码"],
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
    private byte[] _refValue

    /**
     * 返回内部数据报文的数据,经过解码的数据
     * @return
     */
    public String getPacketValue() {
        return new String(Arrays.copyOfRange(_refValue, 0, PACKET_LENGTH), CHART_SET)
    }

    public static Map getPackConfig() {
        return packConfig
    }
    /**
     * 创建一个报文头
     * @param _value 字符串格式的报文头数据
     */
    HEAD(String _value) {
        def temp = _value.getBytes(CHART_SET)
        if (_value == null || temp.length < PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        this._refValue = temp
    }
    /**
     * 创建一个报文头
     * @param _value 字节格式的数据
     */
    HEAD(byte[] _value) {
        if (_value == null || _value.length < PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
//        Charset charset = Charset.forName(CHART_SET)
//        def headBytes = Arrays.copyOfRange(_refValue, 0, PACKET_LENGTH)
        this._refValue = _value
        this.CHART_SET = GetConfig("charset")
    }
    /**
     * 获取报文的字节格式数据
     * @return 报文头的字节数据
     */
    public byte[] getHeadBytes() {
        def result = Arrays.copyOfRange(_refValue, 0, PACKET_LENGTH)
        //对空字符串进行替换
        result.each {
            if (it.byteValue() == OZER)
                it = CHART_NULL
        }
        return result
    }

    //数据内容的长度
    public Long getFileDataLength() { return Long.parseLong(GetConfig("dataLength")) }

    //返回码
    public String getReturnCode() { return GetConfig("returnCode") }

    public String getReturnDescription() { return GetConfig("returnDescription") }

    public String getNumber() { return GetConfig("number").trim() }

    public static String getCharset() { return (CHART_SET) }

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
    public String GetConfig(String mapName) {
        def map = getPackConfig()[mapName]
        def result = new String(Arrays.copyOfRange(_refValue, map.s, map.s + map.l), getCharset())
        //_refValue.substring(map.s, map.s + map.l)
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
    public <T> void SetConfig(String mapName, T value) {
        def location = getPackConfig()[mapName]
        String newValue = ""
        //数字前补0
        if (value.class.name == Integer.class.name) {
            newValue = UtilityString.RequestFormat(value,location.l)
        }
        //字符后补空格
        if (value.class.name == String.class.name) {
            def val = value as String
//            newValue=val.padRight(val.length()-location.l," ")
            newValue = UtilityString.RequestFormat(val, location.l)
    }
        def src = newValue.getBytes(getCharset())
        System.arraycopy(src, 0, _refValue, location.s, src.length)
    }

    /**
     * 获取一个标准请求头
     * @return
     */
    static HEAD GetDefaultRequestHead() {
        return HEAD.CreateHead("A001010101001010799000023420000000000055S001       0120100809171028    2010080981026055                                                                                                          00000                       0")
    }

    public static void main(String[] args) {
//        def bps=new BankProxyService()
//        println(bps.QueryBalance([account: "11007187041901", CcyType: "C", CcyCode: "RMB"]))
//        println(bps.TransatcionRecords([adf: "affasd"]))
//        def status=bps.CheckServerStatus()
//        println(status)
//        println(bps.TransferSingleQuery4005("safa", "dsaf", "dsafa"))
//
//        def s4004 =bps.TransferSing(
//               new Pack4004(_ThirdVoucher: "safasjfoafjaofaojf",_CcyCode: "RMB",_OutAcctNo: "20938420482",_OutAcctName: "Jack")
//        )
//        println(s4004)
    }
}
