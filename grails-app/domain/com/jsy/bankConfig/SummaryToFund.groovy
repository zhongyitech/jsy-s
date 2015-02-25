package com.jsy.bankConfig
/*
* 摘要-账套 对应表
* */
class SummaryToFund {

    //摘要名称
    String sumName

    //科目名称
    String subject

    //借贷
    //在备注上写上 True=借 ，Flase =贷
    boolean borrowAndLend

    //账套
    String fund

    static constraints = {
    }
}
