package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.auth.User
import com.jsy.bankConfig.BankAccount
import com.jsy.customerObject.Customer
import com.jsy.utility.UtilityString

/**
 * 委托付款申请
 */
class Wtfksq {
//    def springSecurityService

    String number=""
    //关联的档案
    InvestmentArchives archives
    //客户
    Customer customer
    //基金名称
    String fundName
    //合同编号
    String htbh
    //申请人
    User sqr
    //申请部门
    String sqbm
    //备注
    String bz

    //募集账户
    BankAccount mjAccount

    //生成日期
    Date scrq = new Date()
    //状态 0:未审核
    int status = 0

    static hasMany = [khfbs: Khfb]

    def beforeInsert() {
//        this.sqr = springSecurityService.getCurrentUser()
        this.sqbm = this.sqr.department?.deptName
        this.number = "JSY-FKET-" + UtilityString.RequestFormat(4, Wtfksq.count())
    }

    static constraints = {
        sqr nullable: true
        bz nullable: true
    }
}
