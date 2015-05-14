package com.jsy.archives

import com.jsy.utility.DateUtility

/**
 * 兑付表 (最后交由银行处理的数据)
 */
class Payment {
    //对应兑付查询id,生成兑付信息的表的ID,表的类型根据dflx不区别
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

    /*类型为管理提成时的附近加信息:支付的是第几次管理提成
    存储的是发放时间的字符串格式
    */
    Date glPayCount

    //发票额
    BigDecimal fpe

    //兑付状态，付款状态，0：未付，1：付款中，2：已付
    int status = 0
    String payStatus = ""
    //转账成功之后的手续费
    String fee1

    //调用银行支付接口后,银行返回的转账流水
    String frontLogNo
    //系统自定义的凭证号:与pack4004请求包的数据关联
    String cstInnerFlowNo

    //支付时间
    Date zfsj
    //已付时间
    Date yfsj

    //生成时间
    Date scsj = new Date()

    //common
    Date dateCreated
    Date lastUpdated

//    def beforeInsert() {
//        scsj=new Date()
//    }
    def beforeInsert() {
        if (this.glPayCount != null) {
            this.glPayCount = DateUtility.lastDayWholePointDate(this.glPayCount)
        }
        if (this.zfsj != null) {
            this.zfsj = DateUtility.lastDayWholePointDate(this.zfsj)
        }
    }
    static constraints = {
        yfsj nullable: true
        frontLogNo nullable: true
        cstInnerFlowNo nullable: true
        glPayCount nullable: true
        fee1 nullable: true
    }
}
