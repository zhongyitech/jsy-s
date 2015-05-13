package com.jsy.fundObject

import com.jsy.project.TSProject
import com.jsy.system.TypeConfig

/**
 * 基金信息表
 */
class Fund {
    //关联项目
    TSProject project

    //基金名称
    String fundName
    //基金编号
    String fundNo
    //开售日期
    Date startSaleDate
    //预募规模
    BigDecimal raiseFunds = 0
    //实募规模 = 0
    BigDecimal rRaiseFunds = 0
    //季付募集规模 = 0
    BigDecimal quarterRaise = 0
    //季付实募 = 0
    BigDecimal rQuarterRaise = 0
    //半年付募集规模 = 0
    BigDecimal halfRaise = 0
    //半年付实募 = 0
    BigDecimal rHalfRaise = 0
    //年付募集规模 = 0
    BigDecimal yearRaise = 0
    //年付实募 =0
    BigDecimal rYearRaise = 0
    //停募日期
    Date stopRaise
    //停募原因
    String stopRaiseReason
    //状态  待售、募集中、停募
    TypeConfig status
    //创建日期
    Date createDate
    //扣除违约金比例
    double kcwyjbl=0.05

    /* 以下基金字段是没用实际用到的，前台界面也没有  */
    //基金责任人：没用到！
    String owner
    //备注：没用到！
    String memo
    //已付利息：没用到！
    BigDecimal interestPaid = 0
    //未付利息 = 0：没用到！
    BigDecimal unpaidInterest = 0
    //已付本金 = 0：没用到！
    BigDecimal paidPrincipal = 0
    //未付本金 = 0：没用到！
    BigDecimal unpaidPrincipal = 0
    //转投合计 = 0：没用到！
    BigDecimal investmentDiversion = 0
    //退伙合计 = 0：没用到！
    BigDecimal exitInvestment = 0
    //续投合计 = 0：没用到！
    BigDecimal reinvestment = 0
    //投资日期：没用到！
    Date investDate
    //应投项目金额：没用到！
    String investmentAmount
    //实投项目金额：没用到！
    BigDecimal rInvestmentAmount = 0
    //已收管理费 = 0：没用到！
    BigDecimal managementFee = 0
    //已收渠道费：没用到！
    BigDecimal channelFee = 0
    //已收项目利息：没用到！
    BigDecimal collectedInterest = 0
    //未收项目利息 = 0：没用到！
    BigDecimal uncollectedInterest = 0
    //已收项目本金 = 0：没用到！
    BigDecimal collectedPrincipal = 0
    //未收项目本金 = 0：没用到！
    BigDecimal uncollectedPrincipal = 0




    //有限合伙
    FundCompanyInformation funcCompany


    //相关联项目
    static belongsTo = []

    static hasMany = [
            yieldRange:YieldRange,  //收益率范围
            tcfpfw:Tcfpfw,          //提成分配
            kxzqx:Kxzqx             //可选投资期限
    ]

    //收益率范围
    //非包销情况下调用返回收益率
    double getYieldRange(BigDecimal investment,String type){
        double y=0
        this.yieldRange.each {
//            print(it.investment1)
//            print(it.investment2)
//            print(investment)
            if(type.equalsIgnoreCase(it.vers)&&(investment<(it.investment2==0?9999999999999999:it.investment2))&&(investment>=it.investment1)){
                y = it.yield
            }
        }
        return y
    }

    static constraints = {
        project nullable: true
        fundName unique: true,nullable: false
        fundNo nullable: true
        startSaleDate nullable: false
        raiseFunds nullable: true
        rRaiseFunds nullable: true
        quarterRaise nullable: true
        rQuarterRaise nullable: true
        halfRaise nullable: true
        rHalfRaise nullable: true
        yearRaise nullable: true
        rYearRaise nullable: true
        stopRaise nullable: true
        stopRaiseReason nullable: true
        createDate nullable: true
        owner nullable: true
        memo nullable: true
        interestPaid nullable: true
        unpaidInterest nullable: true
        paidPrincipal nullable: true
        unpaidPrincipal nullable: true
        investmentDiversion nullable: true
        exitInvestment nullable: true
        reinvestment nullable: true
        investDate nullable: true
        investmentAmount nullable: true
        rInvestmentAmount nullable: true
        managementFee nullable: true
        channelFee nullable: true
        collectedInterest nullable: true
        uncollectedInterest nullable: true
        collectedPrincipal nullable: true
        uncollectedPrincipal nullable: true
        //todo:remove this
        funcCompany nullable: true
    }


    def beforeInsert() {
        if (project && !project.fund) {
            project.fund = this
            project.save(failOnError: true)
        }

    }


    def beforeUpdate() {
        if (project && !project.fund) {
            project.fund = this
            project.save(failOnError: true)
        }
    }
}
