package com.jsy.utility

import java.security.PublicKey

/**
 * Created by lioa on 2015/4/30.
 */
enum INVESTMENT_SPEICAL_STATUS {

    Normal(0), WTFK(1), DQZT(2), WDQZT(3), JJXT(4), TH(5), UNION(6)

    private int _v
    private String[] _statusText =
            ["正常", "委托付款", "到期转投", "未到期转投", "基金续投", "退伙申请", "合并申请"]

    INVESTMENT_SPEICAL_STATUS(int _v) {
        this._v = _v
    }

    public GetStatusText(int dazt) {
        if(dazt >=0 && dazt<_statusText.length){
            return  _statusText[dazt]
        }
        return ""
    }
}