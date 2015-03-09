package com.jsy.project

import com.jsy.auth.User
import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile

/**
 * 付款记录
 */
class PayRecord {

    //付款日期
    Date payDate

    //投资金额
    double amount

    String payType

    String pdesc;

    BankAccount bankAccount

    //common
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
        project: TSProject,
        fund : Fund,  //基金
    ];

    static constraints = {
        pdesc nullable: true
        payType: ["borrow", "invest"]
    }

    //应收管理费
    double getMaintainBill(){

    }

    //应收渠道费
    double getCommunicateBill(){

    }

    //投资天数
    int getInvestedDayCounts(){

    }



}
