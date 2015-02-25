package com.jsy.bankConfig

import com.jsy.archives.Payment
import com.jsy.fundObject.Fund
import com.jsy.system.TypeConfig


// 付款记录表
class PaymentRecord {

    //基金
    Fund fund

    //兑付表
    Payment payment

    //付款开户行
    String paymentBankName

    //付款账号
    String paymentAccount

    //付款户名
    String paymentAccountName

    //收款开户行
    String receiptBankName

    //收款账号
    String receiptAccount

    //收款户名
    String receiptAccountName

    //金额
    String sum

    //付款时间
    Date payTime

    //数据生成时间
    Date createTime

    //实际支付时间
    Date reallyPayTime

    //银行流水号
    String bankTransactionCode

    //处理状态 0未付  1已付  2取消
    TypeConfig dealStatus

    //备注
    String remark



    static constraints = {
        bankTransactionCode nullable: true
        remark nullable: true
    }

}
