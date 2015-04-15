package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund

/**
 * 收款记录
 */
class ReceiveRecord {
    //是否删除
    boolean archive = false;

    //付款日期
    Date receiveDate

    //付款金额
    BigDecimal amount

    //款项性质
//    String[] allPayTargets =["本金","第一年利息","管理费","渠道费","逾期利息","违约金","借款"]
    String[] allPayTargets =["original","firstyear","maintain","channel","overdue","penalty","borrow"]

    //银行账户
    BankAccount bankAccountFrom
    BankAccount bankAccountTo

    //本次收款用到账户余额多少
    BigDecimal useOverRecvAmount = 0

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

                bankName:bankAccountFrom.bankName,              //    银行名称
                bankOfDeposit:bankAccountFrom.bankOfDeposit,    //    开户行
                accountName:bankAccountFrom.accountName,        //    户名
                account: bankAccountFrom.account+"("+bankAccountFrom.overReceive+")",               //    账号

                bankNameTo:bankAccountTo.bankName,              //    银行名称
                bankOfDepositTo:bankAccountTo.bankOfDeposit,    //    开户行
                accountNameTo:bankAccountTo.accountName,        //    户名
                accountTo: bankAccountTo.account+"("+bankAccountTo.overReceive+")",               //    账号


                fundid:fund.id,
                fundname:fund.fundName,
                projectid:project.id,
                projectname:project.name,

                remain_charge:remain_charge,                //多余的钱

        ]
        rtn
    }
}
