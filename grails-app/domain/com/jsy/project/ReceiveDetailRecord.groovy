package com.jsy.project

/**
 * 收款明细，一汇款多项收款
 */
class ReceiveDetailRecord {

    String target // "original","firstyear","maintain","channel","overdue","penalty","borrow"
    BigDecimal amount


    static constraints = {
    }
}
