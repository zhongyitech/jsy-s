package com.jsy.bankConfig

import com.jsy.fundObject.FundCompanyInformation

/*
*基金（账套）- 银行匹配表
* */

class FundToBank {

    //基金(子公司)
    String fund

    //对应的公司
    FundCompanyInformation companyInformation

    //凭证账套名称
    String orderCommpanyName

    //银行
    String bank

    //银行账号
    String account

    static constraints = {
        bank nullable: true
        account nullable: true
    }
}
