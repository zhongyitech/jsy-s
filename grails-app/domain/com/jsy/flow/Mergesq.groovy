package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.utility.MyException
import com.jsy.utility.UtilityString

/**
 * 合并申请表
 */
class Mergesq {
    /*----------------前台提交的数据-------------------------*/
    //合并档案编号
    String newContractNum
    //合同编号
    String htbh
    String xhtbh
    //备注
    String bz
    //起息日期
    Date unionStartDate
    //实际扣除利息
    BigDecimal real_lx

    /*----------------需要后台写入的数据-------------------------*/
    //申请人
    User sqr
    //申请部门
    String sqbm
    //申请日期
    Date scrq = new Date()
    //基金名称
    String fundName
    //添加合并款
    BigDecimal addAmount
    //总金额
    BigDecimal totalAmount
    //收益率
    BigDecimal totalRate
    //投资期限
    String totalTzqx
    //应扣除利息
    BigDecimal muteLx
    //付息方式
    String totalFxfj

    int status = 0
    String number = ""

    Customer customer

    Long oldArchivesId = 0
    String guid = ""

    def beforeInsert() {
        this.number = "JSY-HB-" + UtilityString.RequestFormat(Mergesq.count(), 4)
        this.guid = UUID.randomUUID()
        this.xhtbh = this.newContractNum
        def fund = Contract.findByHtbh(this.xhtbh).fund
        def amount = this.totalAmount
        if (fund.limitRules == 0) {
            if (amount < fund.minInvestmentAmount) {
                throw new MyException("投资金额不满足基金（" + fund.fundName + "）的最低投资额(" + fund.minInvestmentAmount + ")要求！")
            }
        }
        if (fund.limitRules == 1) {
            if (amount % fund.minInvestmentAmount != 0) {
                throw new MyException("投资金额不满足基金（" + fund.fundName + "）的最低投资额(" + fund.minInvestmentAmount + "X)整数倍要求！")
            }
        }
    }
    static constraints = {
        number nullable: true
        guid nullable: true
        xhtbh nullable: true
    }
}
