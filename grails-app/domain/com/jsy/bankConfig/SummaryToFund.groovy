package com.jsy.bankConfig
/*
* 摘要-账套 对应表
* */

class SummaryToFund {

    //科目名称
    String subject
    //二级科目
    String subjectLevel2
    //三级科目名称
    String subjectLevel3

    //借贷
    //在备注上写上 True=借 ，Flase =贷
    boolean borrow
    //------查询条件----
    //账套
    String company
    //摘要名称
    String sumName
    static constraints = {
        subjectLevel2 nullable: true
        subjectLevel3 nullable: true
    }
}
