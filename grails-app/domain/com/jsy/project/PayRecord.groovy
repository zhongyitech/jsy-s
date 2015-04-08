package com.jsy.project

import com.jsy.auth.User
import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile
import com.jsy.util.CompoundCalculator
import com.jsy.util.Utils

/**
 * 付款记录
 */
class PayRecord {
    //是否删除
    boolean archive = false;

    //付款日期
    Date payDate

    //投资金额
    BigDecimal amount

    //borrow, invest
    String payType

    String pdesc;

    BankAccount bankAccount

    //是不是已经生成逾期应付记录
    boolean isGenOverShouldPay=false

    /*固定产生的费用，这里是固定的参考总额*/
    BigDecimal manage_bill=0                //管理费
    BigDecimal community_bill=0             //渠道费
    BigDecimal penalty_bill=0               //违约金
    BigDecimal borrow_bill=0                //借款
    BigDecimal interest_bill=0              //本金的年利

    /*已付费用，用作累计统计,这个存在的意义主要是不需要再去查询ShouldReceiveRecord或者ReceiveDetailRecord进行统计 */
    BigDecimal totalPayBack=0              //准对这笔钱，总共还款
    BigDecimal payMainBack=0               //本金还款
    BigDecimal interest_pay=0              //已付本金的年利
    BigDecimal manage_pay=0                //已付管理费
    BigDecimal community_pay=0             //已付渠道费
    BigDecimal penalty_pay=0               //已付违约金
    BigDecimal borrow_pay=0                //已付借款
    BigDecimal overDue_pay=0               //已付逾期费


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

    //理论上不存在这个情况
    def beforeUpdate() {
        manage_bill = amount * project.manage_per                       //管理费
        community_bill = amount * project.community_per                 //渠道费
        borrow_bill = amount * project.borrow_per                       //借款
//        penalty_bill = amount * project.penalty_per                     //违约金
        interest_bill = amount * project.interest_per * project.year1   //第一年利率
    }

    //生成应付记录
    def afterInsert(){
        // 本金
        new ShouldReceiveRecord(seq: 1, payRecord:this,target:'original',amount:amount).save(failOnError: true);

        if("borrow".equals(payType)){
            // 借款利息
            new ShouldReceiveRecord(seq: 5, payRecord:this,target:'borrow',amount:amount * project.borrow_per).save(failOnError: true);

        }else if("invest".equals(payType)){
            // 管理费
            new ShouldReceiveRecord(seq: 2, payRecord:this,target:'maintain',amount:amount * project.manage_per).save(failOnError: true);

            // 渠道费
            new ShouldReceiveRecord(seq: 3, payRecord:this,target:'channel',amount:amount * project.community_per).save(failOnError: true);

            // 第一年利息
            new ShouldReceiveRecord(seq: 4, payRecord:this,target:'firstyear',amount:amount * project.interest_per * project.year1).save(failOnError: true);

        }

        if(isOverDate()){
            //逾期利息
            new ShouldReceiveRecord(seq: 7, payRecord:this,target:'overdue',amount:getOverDue()).save(failOnError: true);

            // 违约金
            new ShouldReceiveRecord(seq: 6, payRecord:this,target:'penalty',amount:amount * project.penalty_per).save(failOnError: true);

            //马上就生成了
            isGenOverShouldPay = true
        }
    }


    /**
     * 获取逾期费
     *
     * 从逾期开始时间算起，计算到当前（stopDate）的逾期利息，样例区间：
     *
     * 利息和是interest1,利率和是interestx，
     * 到了Date1到期，仍有amount1本金未还，
     * 到了Date2又还了一笔本金amount2(amount2<amount1)
     * 到了Date2.5又还了一笔逾期费amountx
     * 到了Date2.6又还了一笔年利息费amounty
     * 到了Date3还amount3(amount3=amount1-amount2)终于把本金还请
     * 到了Date4要结算逾期费
     *
     * 逾期费计算方式分：单利/复利/日复利，详情如下，
     * 单利：看未还本金，本金没有还完毕，(不能计算逾期费)，每次还一笔本金都会影响到逾期费的变化，本金还完毕，逾期费就不会变化了
     * amount1*interestx/365*(Date2-Date1)+
     amount3*interestx/365*(Date3-Date2)
     *
     * 复利：看应收费用（欠本金+利息），应收费用没有还完毕，不能计算逾期费，每次还一笔本金都会影响到逾期费的变化，本金还完毕，逾期费就不会变化了
     * (amount1+interest1)*interestx/365*(Date2-Date1)+
     (amount3+interest1)*interestx/365*(Date2.6-Date2)+
     (amount3+interest1-amounty)*interestx/365*(Date3-Date2.6)+
     (interest1-amounty)*interestx/365*(Date4-Date3)
     *
     * 日复利：看应收费用（欠本金+利息），应收费用没有还完毕，不能计算逾期费，每次还一笔本金都会影响到逾期费的变化，本金还完毕，逾期费仍会变化了
     * (这里的日复利算法不准，但只要基础数据正确，不影响下面的计算)
     value1 = (amount1+interest1+oldvalue)*interestx/365从(Date2-Date1)+
     value2 =(amount3+interest1+value1+oldvalue)*interestx/365从(Date2.5-Date2)+
     value3 =(amount3+interest1+value1+value2-amountx)*interestx/365^(Date2.6-Date2.5)+
     value4 =(amount3+interest1+value1+value2+value3-amounty-amountx)*interestx/365^(Date3-Date2.6)+
     value5 =(interest1+value1+value2+value3+value4-amounty-amountx)*interestx/365^(Date4-Date3)
     *
     * @return
     */
    def getOverDue(Date stopDate){
        def over_interest_pay = 0
        Date nowDate = new Date()
        if(stopDate){
            nowDate = stopDate
        }

        Date lastDate = Utils.addYears(payDate,Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

        //利率和
        BigDecimal allinallpre
        if("borrow".equals(payType)){
            //借款利息
            allinallpre = project.borrow_per
        }else {
            //管理费率+渠道费率+本金的年利率
            allinallpre = project.manage_per + project.community_per + project.interest_per
        }

        if(nowDate.after(lastDate)) {//判断超出预定时间
            def owe_money = amount - payMainBack
            def startDate =  lastDate //变化的开始时间,这个时间开始是逾期时间，这段时间有汇款对结果是有影响的
            if(owe_money > 0){
                if("singleCount".equals(project.interestType)){
                    //查询逾期开始时间到现在之间的receive记录
                    def receiveDetailRecords = ReceiveDetailRecord.findAllByTargetAndPayRecordAndDateCreatedBetween("original" , this, startDate, nowDate)
                    if(receiveDetailRecords){
                        receiveDetailRecords.each{receiveDetailRecord->
                            def over_days = Utils.dayDifferent(startDate,receiveDetailRecord.dateCreated)
                            startDate = receiveDetailRecord.dateCreated

                            over_interest_pay += CompoundCalculator.fv(receiveDetailRecord.ownOriginal, allinallpre, over_days)
                        }
                    }else{
                        def over_days = Utils.dayDifferent(startDate,nowDate)
                        over_interest_pay += CompoundCalculator.fv(amount, allinallpre, over_days)
                    }
                }else if("costCount".equals(project.interestType)){
                    //查询逾期开始时间到现在之间的receive记录
                    def receiveDetailRecords = ReceiveDetailRecord.findAllByPayRecordAndDateCreatedBetween(this, startDate, nowDate)
                    if(receiveDetailRecords){
                        receiveDetailRecords.each{receiveDetailRecord->
                            def over_days = Utils.dayDifferent(startDate,receiveDetailRecord.dateCreated)
                            startDate = receiveDetailRecord.dateCreated

                            over_interest_pay += CompoundCalculator.fv(receiveDetailRecord.totalBalance, allinallpre, over_days)
                        }
                    }else {
                        def over_days = Utils.dayDifferent(startDate, nowDate)
                        startDate = nowDate
                        over_interest_pay += CompoundCalculator.fv(totalBalance(), allinallpre, over_days)
                    }

                    //时间是一直都又效的
                    if(startDate.before(nowDate)){
                        def over_days = Utils.dayDifferent(startDate,nowDate)
                        over_interest_pay += CompoundCalculator.fv(totalBalance(), allinallpre, over_days)
                    }
                }else if("dayCount".equals(project.interestType)){
                    if(!project.daycount_per || project.daycount_per<=0){
                        throw new Exception("项目的日复利日利率没有设置！")
                    }
                    //查询逾期开始时间到现在之间的receive记录
                    def receiveDetailRecords = ReceiveDetailRecord.findAllByPayRecordAndDateCreatedBetween(this, startDate, nowDate)
                    if(receiveDetailRecords){
                        receiveDetailRecords.each{receiveDetailRecord->
                            def over_days = Utils.dayDifferent(startDate,receiveDetailRecord.dateCreated)
                            startDate = receiveDetailRecord.dateCreated

                            over_interest_pay += CompoundCalculator.rfv(receiveDetailRecord.totalBalance, project.daycount_per, over_days)
                        }
                    }else{
                        def over_days = Utils.dayDifferent(startDate,nowDate)
                        startDate = nowDate
                        def balance = totalBalance()
                        over_interest_pay += (CompoundCalculator.rfv(balance, project.daycount_per, over_days) - balance)
                    }


                    //时间是一直都有效的
                    if(startDate.before(nowDate)){
                        def over_days = Utils.dayDifferent(startDate,nowDate)
                        def balance = totalBalance()
                        over_interest_pay += (CompoundCalculator.rfv(balance, project.daycount_per, over_days) - balance)
                    }
                }
            }
        }

        over_interest_pay
    }

    boolean isOverDate(Date stopDate){
        Date nowDate = new Date()

        if(stopDate){
            nowDate = stopDate;
        }


        Date lastDate = Utils.addYears(payDate,Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

        if(nowDate.after(lastDate)){
            return true;
        }else{
            return false;
        }
    }

    def getPenaltyBill(Date stopDate) {
        Date nowDate = new Date()
        if (stopDate) {
            nowDate = stopDate
        }

        Date lastDate = Utils.addYears(payDate, Integer.parseInt(new java.text.DecimalFormat("0").format((project.year1 + project.year2))))

        if (nowDate.after(lastDate)) {//判断超出预定时间
            return amount * project.penalty_per
        }else{
            return 0;
        }
    }



    /**
     * 计算总共相差多少钱：应该付款-已经付款
     * @return
     */
    def totalBalance(){
        BigDecimal should_pay=0;
        BigDecimal already_pay=0;

        should_pay+=amount;

        if("borrow".equals(payType)){
            should_pay+=borrow_bill;
        }else if("invest".equals(payType)){
            should_pay+=manage_bill;
            should_pay+=community_bill;
            should_pay+=interest_bill;
        }

        if(isOverDate()){//需要计算逾期费
            should_pay+=getOverDue()
            should_pay+=penalty_bill;

        }

        already_pay+=payMainBack
        if("borrow".equals(payType)){
            already_pay+=borrow_pay
        }else if("invest".equals(payType)){
            already_pay+=interest_pay
            already_pay+=manage_pay
            already_pay+=community_pay
        }

        if(isOverDate()){
            already_pay+=penalty_pay
            already_pay+=overDue_pay
        }

        return should_pay-already_pay
    }


    def getInvestDays(Date stopDate){
        Date nowDate = new Date()

        if(stopDate){
            nowDate = stopDate
        }

        Utils.dayDifferent(payDate,nowDate)
    }

    def getShowProperties(Date stopDate){
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
                isOverDate:isOverDate(stopDate),
                penalty_bill:getPenaltyBill(stopDate),
                borrow_bill:borrow_bill,
                interest_bill:interest_bill,

                fundid:fund.id,
                fundname:fund.fundName,
                projectid:project.id,
                projectname:project.name,


                overDue:getOverDue(stopDate),
                investDays:getInvestDays(stopDate)

        ]
        rtn
    }

}
