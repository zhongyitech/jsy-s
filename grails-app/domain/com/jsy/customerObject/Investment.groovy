package com.jsy.customerObject

import com.jsy.auth.User

/**
 * 客户投资信息
 */
class Investment {

    //投资信息编号
    String investmentNumber
    //档案编号
    String archivesNumber
    //合同编号
    String contractNumber
    //基金名称
    String fundName
    //基金编号
    String fundNumber
    //投资金额
    String investmentSum
    //投资期限
    String investmentDeadline
    //认购日期
    Date BuyDate
    //到期日期
    Date maturityDate
    //付息方式
    String paymentType
    //付息次数
    String paymentSum
    //年化收益率
    String yearYields
    //合同状态
    String contractStatus
    //备注
    String remark
    //理财经理
    User financialManager
    //部门经理
    User departmentManager
    //地区
    String area
    //公司id

    //部门id

    //客户名称

    //客户id

    //录入人
    User Entryer
    //录入时间
    Date entryTime
    //审核人
    User reviewer
    //审核时间
    Date reviewTime
    //最近一次修改时间
    Date recentAlter
    //修改人
    User alterUser
    //是否来自转投
    boolean fromTransfer
    //原投资信息编号
    String oldInvestmentNumber
    //是否来自续投
    boolean  fromContinue
    //续投的投资信息编号
    String oldContinueNumber
    //是否委托付款
    boolean  trustPayment
    //是否委托收款
    boolean  trustEarnings
    //是否退伙
    boolean isWithdraw
    //是否转投出去
    boolean  outTransfer
    //是否续投
    boolean  outContinue
    //已付利息
    String paidPayment
    //未付利息
    String noPaidPayment
    //已付本金
    String paidCapital
    //未付本金
    String noPaidCapital
    //业务提成金额
    String salesmanBonus
    //管理提成金额
    String managerBonus
    //投资确认书是否已打印
    boolean isPrint

    static constraints = {
    }
}
