package com.jsy.bankConfig

class BankOrder {

    //凭证编号
    String evidenceCode

    //帐套(公司)
    String fund

    //凭证日期
    Date evidenceDate

    //凭证字
    String evidenceKey

    //凭证号
    String evidenceValue

    //生成时间
    Date createDate

    //处理状态 0未处理  1已处理  2取消
    int manageType

    //处理时间
    Date manageDate

    def beforeInsert(){
        createDate = new Date()

    }
    static constraints = {
        createDate nullable: true
    }
}
