package com.jsy.bankConfig

import com.jsy.archives.CustomerArchives
import com.jsy.fundObject.FundCompanyInformation
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

    BigDecimal overReceive = 0 //收款时，往这个帐号收款，多收的钱

    /**
     * 冗余设计
     */
    String purposeName

    FundCompanyInformation companyInformation
    CustomerArchives customerArchives

    static constraints = {
        purposeName nullable: true
        companyInformation nullable: true
        customerArchives nullable: true
    }


    def beforeInsert() {
        if (purpose) {
            purposeName = purpose.mapName
        }
    }


    def beforeUpdate() {
        if (purpose) {
            purposeName = purpose.mapName
        }else{
            purposeName = ""
        }
    }
}
