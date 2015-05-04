package com.jsy.bankServices

/**
 * ��������ͷ 227λ
 * Created by lioa on 2015/5/4.
 */
class FILEHEAD {
    public final int PACKET_LENGTH = 227
    public final String CHART_SET = "GBK"
    static final Map packConfig = [
            fileName       : [s: 0, l: 6, dest: "�ļ�����"],
            charset        : [s: 240, l: 2, data: ["01": "GBK", "02": "UTF8", "03": "unicode", "04": "iso-8859-1"], dest: "���ı���"],
            downType       : [s: 242, l: 1, data: ["0": "default", "1": "fileSystem", "2": "FTP", "3": "HTTP"], dest: "��ȡ�ļ���ʽ"],
            isToken        : [s: 243, l: 1, data: ["0": "��ǩ��", "1": "ǩ��"], dest: "�Ƿ���ļ�ǩ��"],
            tokenType      : [s: 244, l: 1, dest: "ǩ����������ʽ", data: ["1": "no", "2": "PKCS7"]],
            tokenLength    : [s: 257, l: 12, dest: "ǩ�����ݳ���"],
            fileDataLength : [s: 267, l: 10, dest: "�ļ����ݳ���"],
    ]
}
