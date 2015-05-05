package com.jsy.bankServices


import java.nio.charset.Charset

/**
 * 附件报文头 227位
 * Created by lioa on 2015/5/4.
 */
class FILEHEAD {
    public static final int PACKET_LENGTH = 277
    public static String CHART_SET = "GBK"
    static final Map packConfig = [
            fileName      : [s: 0, l: 6, dest: "文件名称"],
            charset       : [s: 240, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "报文编码"],
            downType      : [s: 242, l: 1, data: ["0": "default", "1": "fileSystem", "2": "FTP", "3": "HTTP"], dest: "获取文件方式"],
            isToken       : [s: 243, l: 1, data: ["0": "不签名", "1": "签名"], dest: "是否对文件签名"],
            tokenType     : [s: 244, l: 1, dest: "签名数据名格式", data: ["1": "no", "2": "PKCS7"]],
            tokenLength   : [s: 257, l: 12, dest: "签名内容长度"],
            fileDataLength: [s: 267, l: 10, dest: "文件内容长度"],
    ]

    //数据的引用
    private byte[] _refValue
    private int _startIndex

    FILEHEAD(byte[] bytes, HEAD head) {
        _startIndex = head.PACKET_LENGTH + head.getFileDataLength()
        if (bytes == null || bytes.length < _startIndex+PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        this._refValue = bytes
    }

    FILEHEAD(String value) {
        _startIndex =0
        def bytes=value.getBytes(CHART_SET)
        if (bytes == null || bytes.length < _startIndex) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        this._refValue = bytes
        CHART_SET=GetConfig("charset")
    }

    public Long getFileDataLength() {
        return Long.parseLong(GetConfig("fileDataLength"))
    }

    public String getCharset() { return CHART_SET }

    public String GetConfig(String mapName) {
        def map = getPackConfig()[mapName]
        def result = new String(Arrays.copyOfRange(_refValue, _startIndex + map.s,_startIndex+ map.s + map.l), getCharset())
        //_refValue.substring(map.s, map.s + map.l)
        if (map.data) {
            println(map.dest + ":" + map.data[result] + " (" + result + ")")
            return map.data[result]
        } else {
            println(map.dest + ":" + result)
        }
        return result
    }

    static FILEHEAD CreateFILEHEAD(byte[] refValue, HEAD head) {
        return new FILEHEAD(refValue, head)
    }

    public byte[] getHeadBytes() {
        return Arrays.copyOfRange(_refValue, _startIndex, PACKET_LENGTH)
    }
}
