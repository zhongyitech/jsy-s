package com.jsy.utility

/**
 * 投资档案的付息方式
 * Created by lioa on 2015/5/12.
 */
enum INVESTMENT_PAYTYPE {
    Year('Y'), Done('N'), Quarter('J'), Moth('M'), Now('D'), HalfYear('W')

    private String _v
    private Map _statusText =
            [Y: "年付", N: "到期付", J: "季付", M: "月付", D: "立即付息", W: "半年付"]

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