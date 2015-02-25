package com.jsy.archives

import com.jsy.system.TypeConfig

/**
 * 兑付表
 */
class Payment {
    //对应兑付查询id
    Long infoId
    //基金名称
    String fundName
    //合同编号
    String contractNum
    //客户名称
    String customerName
    //应付款
    BigDecimal yfk
    //开户行
    String khh
    //账号
    String zh
    //部门经理
    String bmjl
    //为本金还是利息的兑付，值为bj或者lx   为管理提成gl为业务提成yw
    String dflx

    //发票额
    BigDecimal fpe

    //兑付状态
    int  status=0
    //支付时间
    Date zfsj
    //已付时间
    Date yfsj

    //生成时间
    Date scsj=new Date()
//    def beforeInsert() {
//        scsj=new Date()
//    }

    static constraints = {
        yfsj nullable: true
    }
}
