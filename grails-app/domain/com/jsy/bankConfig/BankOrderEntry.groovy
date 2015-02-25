package com.jsy.bankConfig

class BankOrderEntry {

    //凭证编号
    String evidenceCode

    //摘要
    String summary

    //科目名称
    String subjectName

    //贷方金额
    BigDecimal lendAmount

    //借方金额
    BigDecimal borrowAmount

    //交易流水
    String transaction


    static constraints = {
    }
}
