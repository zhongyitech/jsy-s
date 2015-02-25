package com.jsy.fundObject

import com.jsy.bankConfig.BankAccount

//基金公司信息表
class FundCompanyInformation {

    //基金
    Fund fund

    //公司名称
    String companyName

    //项目
    //未知

    //法人代表
    String corporate

    //注册地址
    String address

    //募集账户
    static hasMany = [bankAccount:BankAccount]

    static constraints = {
    }
}
