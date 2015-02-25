package com.jsy.flow
/**
 * 客户副表，收款人付款人一类
 */
class Khfb {

    //付款金额
    BigDecimal fkje
    //付款日期
    Date fkrq

    //证件类型
    int type
    //名称
    String name
    //法定代表人
    String fddbr
    //证件号
    String zjhm


    static constraints = {
        fddbr nullable: true
    }
}
