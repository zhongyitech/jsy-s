package com.jsy.utility

/**
 * Ͷ�ʵ����ĸ�Ϣ��ʽ
 * Created by lioa on 2015/5/12.
 */
enum INVESTMENT_PAYTYPE {
    Year('Y'), Done('N'), Quarter('J'), Moth('M'), Now('D'), HalfYear('W')

    private String _v
    private Map _statusText =
            [Y: "�긶", N: "���ڸ�", J: "����", M: "�¸�", D: "������Ϣ", W: "���긶"]

    INVESTMENT_PAYTYPE(
            String _v
    ) {
        this._v = _v
    }

    def GetStatusText(String type) {
        if (_statusText.containsKey(type))
            return _statusText[type]
        return ""
    }

    String getValue() { return _v }
}