package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer

/**
 * 合并申请表
 */
class Mergesq {
    /*----------------前台提交的数据-------------------------*/
    //合并档案编号
    String newContractNum
    //合同编号
    String htbh
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
    Date sqrq = new Date()
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

    Customer customer

    static constraints = {
    }
}
