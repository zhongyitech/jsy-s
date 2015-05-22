package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.customerObject.Customer
import com.jsy.system.TypeConfig
import com.jsy.utility.UtilityString

/**
 * 委托收款申请 [已不使用)
 * Created by 嘉文 on 2014/12/24.
 */
class EntrustReceivable {

    String number=""
    //投资档案
    InvestmentArchives investmentArchives

    //客户信息
    Customer customer

    //收款人信息
    //姓名
    String ReceivableName

    //投资款
    BigDecimal InvestmentAmount

    //收益额
    BigDecimal IncomeAmount

    //证件类型
    String CertificateType

    //证件号码/注册号
    String CertificateNumber

    //法定代表人
    String LawPeople

    //委托人收款账户信息
    //收款人开户行
    String ReceivableBank

    //收款人账号
    String ReceivableAccount

    //特殊情况备注说明
    String Remark

    //状态
    TypeConfig typeConfig

    def beforeInsert() {
        this.number = "JSY-DQZT-" + UtilityString.RequestFormat(EntrustReceivable.count(),4)
    }

    static constraints = {

    }
}
