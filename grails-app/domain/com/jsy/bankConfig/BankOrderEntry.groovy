package com.jsy.bankConfig

//记账凭证副表
class BankOrderEntry {

    //凭证编号
    String evidenceCode

    //摘要
    String summary

    //科目名称
    String subjectName

    //贷方金额
    BigDecimal lendAmount=0

    //借方金额
    BigDecimal borrowAmount=0

    //交易流水
    String transaction

    //生成时间
    Date createDate=new Date()

    String company

    Date processedDate

    int manageType=0

    static constraints = {
        evidenceCode nullable: true
        processedDate nullable: true
    }
    def beforeInsert(){
        createDate = new Date()
        manageType=0
    }
}
