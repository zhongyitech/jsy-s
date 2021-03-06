package com.jsy.bankConfig

/**
 * 银行流水表
 */
class BankTransactionsRecord {

//    数据写入时间（自动设置）
    Date createDate

//    银行名称
    String bankName

//    开户行
    String bankOfDeposit

//    户名
    String accountName

//    账号
    String account

//    对方账号
    String otherSideAccount

//    对方户名
    String otherSideName

//    发生额
    BigDecimal actionAmount

//    余额
    BigDecimal balance

    //借贷
    //在备注上写上 True=借 ，Flase =贷
    boolean borrowAndLend

//    摘要 （可空）
    String summary

//    流水号 （可空）
    String transactionsCode

//    凭证号 （可空）
    String evidenceCode

//    交易时间
    Date dealDate

//    是否处理（Bool）true为已处理 false为未处理
    Boolean managed

//    处理状态 0未处理  1已处理  2放弃
    int manageType = 0

    //数据处理时间
    Date processedDate

//    备注 （可空）
    String remark

    //处理情况的描述
    String processDescript

    def beforeInsert() {
        createDate = new Date()
    }
    static constraints = {
        createDate nullable: true
        processedDate nullable: true
        summary nullable: true
        transactionsCode nullable: true
        evidenceCode nullable: true
        remark nullable: true
        processDescript nullable: true
    }
    /**
     * 记录已经处理的状态设置
     */
    void setProcessedOK() {
        managed = true
        processedDate = new Date()
        manageType = 1
    }

    /**
     * 记录弃用的状态设置
     */
    void setDeletedTag() {
        processedDate = new Date()
        managed = true
        manageType = 2
    }
}
