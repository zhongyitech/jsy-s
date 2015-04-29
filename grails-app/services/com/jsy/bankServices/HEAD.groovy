package com.jsy.bankServices

/**
 * 通讯报文头
 * Created by lioa on 2015/4/24.
 */
class HEAD {
    private String _value

    HEAD(String _value) {
        this._value = _value
    }
    //报文版本
    String Version
    //目标系统
    String TargetSystem
    //报文编码
    String EncodingStr
    //通讯协议
    String Protocol
    //外联客户代码
    //接收报文长度
    //

}
