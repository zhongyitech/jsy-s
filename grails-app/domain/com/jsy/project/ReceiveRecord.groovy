package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund

/**
 * 收款记录
 */
class ReceiveRecord {

    //付款日期
    Date receiveDate

    //付款金额
    BigDecimal amount

    //款项性质
//    String[] allPayTargets =["本金","第一年利息","管理费","渠道费","逾期利息","违约金","借款"]
    String[] allPayTargets =["original","firstyear","maintain","channel","overdue","penalty","borrow"]

    //银行账户
    BankAccount bankAccount

    //本次付款，系统计算多余的钱，这里可能未负数
    BigDecimal remain_charge

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

    def getShowProperties(){
        def rtn = [
                id:id,
                amount:amount,
                receiveDate:receiveDate,

                bankName:bankAccount.bankName,              //    银行名称
                bankOfDeposit:bankAccount.bankOfDeposit,    //    开户行
                accountName:bankAccount.accountName,        //    户名
                account: bankAccount.account,               //    账号


                fundid:fund.id,
                fundname:fund.fundName,
                projectid:project.id,
                projectname:project.name,

                remain_charge:bankAccount.overReceive,                //多余的钱

        ]
        rtn
    }
}
