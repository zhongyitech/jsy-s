package com.jsy.flow

import com.jsy.utility.UtilityString

/**
 * 客户副表，收款人付款人一类
 */
class Khfb {

    String number = ""
    //付款金额
    BigDecimal payAmount
    //付款日期
    Date payDate

    //受托人
    String name

    //付款账号
    String payBankAccount

    //证件类型
    String cardType

    //证件号码
    String cardSn

    //法定代表人
    String fddbr

    //住址
    String address

    def beforeInsert() {
        this.number = "JSY-HB-" + UtilityString.RequestFormat( Mergesq.count(),4)
    }
    static constraints = {
        fddbr nullable: true
        address nullable: true
    }
}
