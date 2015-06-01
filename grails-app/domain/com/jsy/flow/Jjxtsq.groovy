package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.system.TypeConfig
import com.jsy.utility.MyException
import com.jsy.utility.UtilityString

/**
 * 基金续投申请
 */
class Jjxtsq {
//    def springSecurityService

    String number = ""
    //旧档案id
    Long oldArchivesId
    //新档案id
    Long newArchivesId

    //客户
    Customer customer
    //基金名称
    String fundName
    //合同编号
    String htbh
    //新合同编号
    String xhtbh
    //认购日期
    Date rgrq
    //到期日期
    Date dqrq
    //认购金额
    BigDecimal rgje
    //基金状态
    String typeConfig
    //申请人
    User sqr
    //申请部门
    String sqbm

    //续投申请日期
    Date xtsqrq
    //续投本金额
    BigDecimal xtbje
    //追加投资金额
    BigDecimal zjtzje
    //续投收益额
    BigDecimal xtsye
    //续投投资期限
    int xttzqx
    //总投资金额
    BigDecimal ztzje
    //续投计息日期
    Date xtjxrq
    //续投到期日期
    Date xtdqrq
    //续投预期收益率
    double xtyqsyl
    //续投计息方式
    String xtjxfs

    //备注
    String bz

    //生成日期
    Date scrq = new Date()
    //状态
    int status = 0

    //续投类型
    int xtType = 0

    String guid = ""

    def beforeInsert() {
//        this.sqr=springSecurityService.getCurrentUser()
        this.number = "JSY-XT-" + UtilityString.RequestFormat(Dqztsq.count(), 4)
        this.guid = UUID.randomUUID()
        def fund = Contract.findByHtbh(this.xhtbh).fund
        def amount = this.ztzje
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
        sqr nullable: true
        oldArchivesId unique: true
        bz nullable: true
        newArchivesId nullable: true
    }
}
