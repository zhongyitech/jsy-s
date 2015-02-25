package com.jsy.flow

import com.jsy.auth.User
import com.jsy.customerObject.Customer

/**
 * 退伙处理申请
 */
class Thclsq {
    def springSecurityService

    //旧档案id
    Long oldArchivesId
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

    //已付收益情况
    String yfsysjd
    BigDecimal yfsyze
    //申请退伙日期
    Date sqthrq
    //退伙金额
    BigDecimal thje

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

    //收款人帐号信息
    //开户行
    String khh
    //收款人名
    String skr
    //银行帐号
    String yhzh

    //备注
    String bz
    //申请人
    User sqr
    //申请部门
    String sqbm

    //生成日期
    Date scrq=new Date()
    //状态
    int status=0

    def beforeInsert() {
        this.sqr=springSecurityService.getCurrentUser()
    }

    static constraints = {
        oldArchivesId unique: true
        sqr nullable: true
        bz nullable: true
    }
}
