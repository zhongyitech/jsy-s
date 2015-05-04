package com.jsy.bankServices

/**
 * 附件报文头 227位
 * Created by lioa on 2015/5/4.
 */
class FILEHEAD {
    public final int PACKET_LENGTH = 227
    public final String CHART_SET = "GBK"
    static final Map packConfig = [
            fileName       : [s: 0, l: 6, dest: "文件名称"],
            charset        : [s: 240, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "报文编码"],
            downType       : [s: 242, l: 1, data: ["0": "default", "1": "fileSystem", "2": "FTP", "3": "HTTP"], dest: "获取文件方式"],
            isToken        : [s: 243, l: 1, data: ["0": "不签名", "1": "签名"], dest: "是否对文件签名"],
            tokenType      : [s: 244, l: 1, dest: "签名数据名格式", data: ["1": "no", "2": "PKCS7"]],
            tokenLength    : [s: 257, l: 12, dest: "签名内容长度"],
            fileDataLength : [s: 267, l: 10, dest: "文件内容长度"],
    ]
}
