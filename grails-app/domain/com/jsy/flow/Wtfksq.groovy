package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund

/**
 * 委托付款申请
 */
class Wtfksq {
    def springSecurityService

    //相关档案id
    Long archivesId

    //客户
    Customer customer
    //基金名称
    String fundName
    //合同编号
    String htbh
    //认购日期
    Date rgrq
    //认购金额
    BigDecimal rgje
    //申请人
    User sqr
    //申请部门
    String sqbm

    //备注
    String bz

    //生成日期
    Date scrq=new Date()
    //状态
    int status=0

    static hasMany = [khfbs:Khfb]

    def beforeInsert() {
        this.sqr=springSecurityService.getCurrentUser()
    }

    static constraints = {
        sqr nullable: true
        archivesId unique: true
        bz nullable: true
    }
}
