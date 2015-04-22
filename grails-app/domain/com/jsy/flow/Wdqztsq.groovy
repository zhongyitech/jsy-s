package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import grails.plugin.springsecurity.SpringSecurityService

/**
 * 未到期转投
 */

class Wdqztsq {
//    def springSecurityService

    //客户
    Customer customer
    //旧档案id
    Long oldArchivesId
//    新档案id
    Long newArchivesId

    //基金名称
    String fundName
    //合同编号
    String htbh
    //认购日期
    Date rgrq
    //到期日期
    Date dqrq
    //认购金额
    BigDecimal rgje
    //已付收益情况
    String yfsysjd
    BigDecimal yfsyze

    //新转入基金信息
    Fund ztjj
    //新基金名称
    String xjjmc
    //合同编号
    String xhtbh
    //转投金额
    BigDecimal ztje

    //退出原基金情况
    //合同中约定扣除违约金比例
    double kcwyjbl
    //扣除违约金金额
    BigDecimal kcwyj
    //合同中约定预期收益率
    double ydyqsyl
    //需付预期收益额
    BigDecimal yqsye
    //应回收业务提成比例
    double ywtchsbl
    //业务提成回收金额
    BigDecimal ywtchsje
    //应回收管理提成比例
    double gltchsbl
    //管理提成回收金额
    BigDecimal gltchsje

    //备注
    String bz = ""
    //申请人
    User sqr
    //申请部门
    String sqbm

    //转投本金额
    BigDecimal ztbje = 0
    //转投剩余收益额
    BigDecimal ztsysye = 0

    //生成日期
    Date scrq = new Date()
    //状态
    int status = 0

    def beforeInsert() {
        this.sqr =new SpringSecurityService().getCurrentUser()
    }
    static constraints = {
        sqr nullable: true
        newArchivesId nullable: true
        oldArchivesId unique: true
        bz nullable: true
    }
}
