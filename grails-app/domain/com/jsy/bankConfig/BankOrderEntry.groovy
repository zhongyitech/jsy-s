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

    //交易时间
    Date dealDate

    //生成时间
    Date createDate=new Date()

    //账套(公司名称)
    String company

    //消息处理时间
    Date processedDate

    //处理情况
    int manageType=0

    //备注
    String bz=""

    static constraints = {
        evidenceCode nullable: true
        processedDate nullable: true
        bz nullable: true
    }
    def beforeInsert(){
        createDate = new Date()
        manageType=0
    }
}
