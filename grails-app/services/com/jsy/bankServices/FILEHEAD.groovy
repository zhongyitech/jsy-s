package com.jsy.bankServices

import grails.plugin.asyncmail.Validator

import java.nio.charset.Charset

/**
 * 附件报文头 227位
 * Created by lioa on 2015/5/4.
 */
class FILEHEAD {
    public static final int PACKET_LENGTH = 277
    public static final String CHART_SET = "GBK"
    static final Map packConfig = [
            fileName      : [s: 0, l: 6, dest: "文件名称"],
            charset       : [s: 240, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "报文编码"],
            downType      : [s: 242, l: 1, data: ["0": "default", "1": "fileSystem", "2": "FTP", "3": "HTTP"], dest: "获取文件方式"],
            isToken       : [s: 243, l: 1, data: ["0": "不签名", "1": "签名"], dest: "是否对文件签名"],
            tokenType     : [s: 244, l: 1, dest: "签名数据名格式", data: ["1": "no", "2": "PKCS7"]],
            tokenLength   : [s: 257, l: 12, dest: "签名内容长度"],
            fileDataLength: [s: 267, l: 10, dest: "文件内容长度"],
    ]

    private String _value = ""

    FILEHEAD(String _value) {
        this._value = _value
    }

    FILEHEAD(byte[] bytes) {
        if (bytes == null || bytes.length < PACKET_LENGTH) {
            throw new Exception("报文数据不能为空或长度必须是:" + PACKET_LENGTH + "个字节.")
        }
        Charset charset = Charset.forName(CHART_SET)
        def headBytes = Arrays.copyOfRange(bytes, 0, PACKET_LENGTH)
        this._value = new String(headBytes, charset)
    }

    public Long getFileDataLength() {
        return Long.parseLong(GetConfig(getPackConfig().fileDataLength))
    }

    public String getCharset() { return GetConfig(getPackConfig().charset) }

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

    static FILEHEAD CreateFILEHEAD(byte[] value) {
        if (value.length >= PACKET_LENGTH) {
            return new FILEHEAD(value)
        }
        throw new Exception("附件报文头格式不正确")
    }

    public byte[] getHeadBytes(Charset charset) {
        return _value.getBytes(charset)
    }
}
