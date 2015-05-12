package com.jsy.flow
/**
 * 客户副表，收款人付款人一类
 */
class Khfb {

    //付款金额
    BigDecimal payAmount
    //付款日期
    Date payDate

    //受托人
    String pName

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
    static constraints = {
        fddbr nullable: true
        address nullable: true
    }
}
