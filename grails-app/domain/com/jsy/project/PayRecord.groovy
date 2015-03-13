package com.jsy.project

import com.jsy.auth.User
import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile
import com.jsy.util.Utils

/**
 * 付款记录
 */
class PayRecord {

    //付款日期
    Date payDate

    //投资金额
    BigDecimal amount

    String payType

    String pdesc;

    BankAccount bankAccount

    double totalPayBack     //准对这笔钱，总共还款
    double payMainBack      //本金还款

    /*固定产生的费用，这里是固定的参考总额*/
    BigDecimal manage_bill                //管理费
    BigDecimal community_bill             //渠道费
    boolean isOverDate=false              //是否逾期，如果是，则会产生违约金，是固定的，并且根据超过的时间，会产生逾期费
    BigDecimal penalty_bill               //违约金
    BigDecimal borrow_bill                //借款
    BigDecimal interest_bill              //本金的年利

    //common
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
        project: TSProject,               //项目
        fund : Fund,                      //基金
    ];

    static constraints = {
        pdesc nullable: true
        manage_bill nullable: true
        community_bill nullable: true
        penalty_bill nullable: true
        borrow_bill nullable: true
        interest_bill nullable: true
        payType: ["borrow", "invest"]
    }

    def beforeInsert() {
        manage_bill = amount * project.manage_per                       //管理费
        community_bill = amount * project.community_per                 //渠道费
        borrow_bill = amount * project.borrow_per                       //借款
//        penalty_bill = amount * project.penalty_per                     //违约金
        interest_bill = amount * project.interest_per * project.year1   //第一年利率
    }


    def beforeUpdate() {
        manage_bill = amount * project.manage_per                       //管理费
        community_bill = amount * project.community_per                 //渠道费
        borrow_bill = amount * project.borrow_per                       //借款
//        penalty_bill = amount * project.penalty_per                     //违约金
        interest_bill = amount * project.interest_per * project.year1   //第一年利率
    }

    /**
     * 获取逾期费
     * @return
     */
    def getOverDue(){
        def over_interest_pay = 0
        Date nowDate = new Date()
        Date lastDate = Utils.addYears(payDate,Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

        if(nowDate.after(lastDate)) {//判断超出预定时间
            def owe_money = amount - payMainBack
            def over_days = Utils.dayDifferent(lastDate,nowDate)
            if(owe_money > 0){
                if("singleCount".equals(project.interestType)){//单利：欠款*interest_per/365*超出的天数
                    over_interest_pay = (owe_money * project.interest_per * over_days / 365)
                }else if("costCount".equals(project.interestType)){//单利：(欠款+欠款*interest_per)*penalty_per/365*超出的天数
                    over_interest_pay = (owe_money * (1+project.interest_per) * over_days / 365)
                }else if("dayCount".equals(project.interestType)){//复利：便历每一天，做加法：第一天:(欠款+欠款*penalty_per)*penalty_per/365*1 ,第二天：第一天的利息*penalty_per/365*1，如此类推
                    over_interest_pay=(owe_money * (1+project.interest_per) / 365);  //第一天
                    for(int i=1;i<over_days;i++){//第二天起
                        over_interest_pay += (over_interest_pay * (1+project.interest_per) / 365);
                    }
                }
            }
        }

        over_interest_pay
    }

    def getInvestDays(){
        Utils.dayDifferent(payDate,new Date())
    }

    def getShowProperties(){
        def balance = 0

        def rtn = [
                id:id,
                payDate:payDate,
                amount:amount,
                payType:payType,

                bankName:bankAccount.bankName,              //    银行名称
                bankOfDeposit:bankAccount.bankOfDeposit,    //    开户行
                accountName:bankAccount.accountName,        //    户名
                account: bankAccount.account,               //    账号

                totalPayBack:totalPayBack,
                payMainBack:payMainBack,
                manage_bill:manage_bill,
                community_bill:community_bill,
                isOverDate:isOverDate,
                penalty_bill:penalty_bill,
                borrow_bill:borrow_bill,
                interest_bill:interest_bill,

                fundid:fund.id,
                fundname:fund.fundName,
                projectid:project.id,
                projectname:project.name,


                overDue:getOverDue(),
                investDays:getInvestDays()

        ]
        rtn
    }

}
