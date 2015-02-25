package com.jsy.customerObject

import com.jsy.fundObject.Fund
import com.jsy.system.UploadFile

/**
 * 客户档案
 */
class Customer {

    //客户名称
    String name
    //国家（地区）
    String country
    //证照类型
    String credentialsType
    //证照号码
    String credentialsNumber
    //身份证地址
    String credentialsAddr
    //法定代表人
    String fddbr
    //注册号
    String zch
    //开户行
    String khh
    //银行账号
    String yhzh
    //联系电话
    String telephone
    //联系手机
    String phone
    //邮政编码
    String postalcode
    //email
    String email
    //通讯地址
    String callAddress
    //客户评价
    String reviews
    //客户资质
    String qualification
    //备注
    String remark
    //累计认购基金数
    BigDecimal buyTotalFund=0
    //累计认购金额
    BigDecimal buyTotalMoney=0
    //累计收益
    BigDecimal totalEarnings=0
    //最早投资日期
    Date beginInvest
    // 最早认购基金
    Fund beginFund
    //最近投资日期
    Date recentlyInvestDate
    //最近认购基金
    Fund recentlyFund
    //最近一笔收益
    BigDecimal recentlyEarnings=0
    //最近一笔收益日期
    Date recentlyEarningsDate
    //是否有过付款委托
    boolean trustPayment=false
    //是否有过收益委托
    boolean  trustEarnings=false

    //附件
    static hasMany = [uploadFiles:UploadFile]

    def beforeUpdate = {
        new CustomerRecord(customerId:this.id,name:this.name,country:country,credentialsType:credentialsType,credentialsNumber:credentialsNumber,fddbr:fddbr,zch:zch,khh:khh,yhzh:yhzh,telephone:telephone,phone:phone,postalcode:postalcode,email:email,callAddress:callAddress).save(failOnError: true)
    }

    static constraints = {
        name nullable: true
        country nullable: true
        credentialsType nullable: true
        khh nullable: true
        yhzh nullable: true
        phone nullable: true

        telephone nullable: true
        postalcode nullable: true
        callAddress nullable: true
        reviews nullable: true
        qualification nullable: true
        remark nullable: true
        beginInvest nullable: true
        beginFund nullable: true
        recentlyInvestDate nullable: true
        recentlyFund nullable: true
        recentlyEarningsDate nullable: true
        fddbr nullable: true
        zch nullable: true
        email nullable:true
    }
}
