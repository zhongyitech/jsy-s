package com.jsy.utility

/**
 * Created by lioa on 2015/4/30.
 */
enum INVESTMENT_SPEICAL_STATUS {

    Normal(0),DQZT(1),WDQZT(2),JJXT(3),TH(4),UNION(5)

    private  int _v
    INVESTMENT_SPEICAL_STATUS(int _v) {
        this._v = _v
    }
}