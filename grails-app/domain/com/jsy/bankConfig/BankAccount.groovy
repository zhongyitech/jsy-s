package com.jsy.bankConfig

import com.jsy.system.TypeConfig

//银行账户表
class BankAccount {

    //    银行名称
    String bankName

//    开户行
    String bankOfDeposit

//    户名
    String accountName

//    账号
    String account

    //用途
    TypeConfig purpose

//是否默认账户
    boolean defaultAccount

    static constraints = {
    }
}
