package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.system.TypeConfig

/**
 * 基金续投申请
 */
class Jjxtsq {
    def springSecurityService

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

    //续投类型
    String xtlx
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
    double  xtyqsyl
    //续投计息方式
    String xtjxfs

    //备注
    String bz

    //生成日期
    Date scrq=new Date()
    //状态
    int status=0

    def beforeInsert() {
        this.sqr=springSecurityService.getCurrentUser()
    }

    static constraints = {
        sqr nullable: true
        oldArchivesId unique: true
        bz nullable: true
        newArchivesId nullable: true
    }
}
