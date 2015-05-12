package com.jsy.archives

/**
 *   ÌØÊâÉêÇë×´Ì¬Ê¶±ğÂë
 * Created by lioa on 2015/4/21.
 */
enum INVESTMENT_STATUS {
    New(0), Normal(1), BackUp(2)
    private int _value

    INVESTMENT_STATUS(int _value) {
        this._value = _value
    }

    public boolean eq(int value) {
        return this._value == value
    }

    int getValue() { return _value }
}