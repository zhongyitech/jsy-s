package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund

/**
 * 合并申请表
 */
class Mergesq {
    //新档案id
    String newContractNum
    //基金名称
    String fundName
    //合同编号
    String htbh
    //备注
    String bz
    //申请人
    User sqr
    //申请部门
    String sqbm
    //申请日期
    Date sqrq = new Date()

    Date unionStartDate

    //实际扣除利息
    BigDecimal real_lx

    static constraints = {
    }
}
