package com.jsy.project

import com.jsy.fundObject.Fund

/**
 * 收款记录
 */
class ReceiveRecord {

    //付款日期
    Date receiveDate

    //付款金额
    double amount

    //款项性质
//    String[] allPayTargets =["本金","第一年利息","管理费","渠道费","逾期利息","违约金","借款"]
    String[] allPayTargets =["original","firstyear","maintain","channel","overdue","penalty","borrow"]
    String payTargets // 格式: original,firstyear,maintain,channel,overdue,penalty,borrow

    String pdesc;

    //common
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
            project: TSProject,
            fund : Fund,  //基金
    ];

    static transients = [ "allPayTargets" ]

    static constraints = {
        pdesc nullable: true
    }


}
