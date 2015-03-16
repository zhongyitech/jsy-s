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



    /*固定产生的费用，这里是固定的参考总额*/
    BigDecimal manage_bill=0                //管理费
    BigDecimal community_bill=0             //渠道费
    BigDecimal penalty_bill=0               //违约金
    BigDecimal borrow_bill=0                //借款
    BigDecimal interest_bill=0              //本金的年利

    /*已付费用，用作累计统计*/
    BigDecimal totalPayBack=0              //准对这笔钱，总共还款
    BigDecimal payMainBack=0               //本金还款
    BigDecimal interest_pay=0              //已付本金的年利
    BigDecimal manage_pay=0                //已付管理费
    BigDecimal community_pay=0             //已付渠道费
    BigDecimal penalty_pay=0               //已付违约金
    BigDecimal borrow_pay=0                //已付借款
    BigDecimal overDue_pay=0                //已付逾期费


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

        totalPayBack nullable: true
        payMainBack nullable: true
        interest_pay nullable: true
        manage_pay nullable: true
        community_pay nullable: true
        penalty_pay nullable: true
        borrow_pay nullable: true
        overDue_pay nullable: true
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

    boolean isOverDate(){
        Date nowDate = new Date()
        Date lastDate = Utils.addYears(payDate,Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

        if(nowDate.after(lastDate)){
            return true;
        }else{
            return false;
        }
    }

    def totalBalance(){
        BigDecimal should_pay=0;
        BigDecimal already_pay=0;

        should_pay+=amount;
        should_pay+=manage_bill;
        should_pay+=community_bill;
        should_pay+=penalty_bill;
        should_pay+=borrow_bill;
        should_pay+=interest_bill;

        if(isOverDate()){//需要计算逾期费
            should_pay+=getOverDue()
        }

        already_pay+=payMainBack
        already_pay+=interest_pay
        already_pay+=manage_pay
        already_pay+=community_pay
        already_pay+=penalty_pay
        already_pay+=borrow_pay
        already_pay+=overDue_pay

        return should_pay-already_pay
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
                isOverDate:isOverDate(),
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
