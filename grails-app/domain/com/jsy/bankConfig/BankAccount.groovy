package com.jsy.bankConfig

import com.jsy.system.TypeConfig

/**
 * 银行账户表
 */

class BankAccount {
    String bankName             //    银行名称
    String bankOfDeposit        //    开户行
    String accountName          //    户名
    String account              //    账号
    boolean defaultAccount = false      //是否默认账户
    TypeConfig purpose          //用途

    /**
     * 冗余设计
     */
    String purposeName


    static constraints = {
        purposeName nullable: true
    }


    def beforeInsert() {
        if (purpose) {
            purposeName = purpose.mapValue
        }
    }


    def beforeUpdate() {
        if (purpose) {
            purposeName = purpose.mapValue
        }else{
            purposeName = ""
        }
    }
}
