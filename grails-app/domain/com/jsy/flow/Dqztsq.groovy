package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund

/**
 * 到期转投申请
 */
class Dqztsq {
//    def springSecurityService
    //客户
    Customer customer
    //旧档案id
    Long oldArchivesId
    //新档案id
    Long newArchivesId
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
    Double rgje
    //转投基金
    Fund ztjj
    //转投基金编号
    //String ztjjbh
    //转投金额
    Double ztje
    //转投收益额
    Double ztsye
    //备注
    String bz
    //申请人
    User sqr
    //申请部门
    String sqbm
    //申请日期
    Date sqrq

    //生成日期
    Date scrq=new Date()
    //状态 0,待审核 1,审核通过
    int status=0

//    def beforeInsert() {
//        this.sqr= springSecurityService.getCurrentUser()
//    }
    static constraints = {
        sqr nullable: true
        oldArchivesId unique: true
        bz nullable: true
        newArchivesId nullable: true
    }
}
