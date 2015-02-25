package com.jsy.flow
/**
 * 委托付款记录，用于对账
 */
class Wtfkjl {
    //对应的档案编号
    Long archivesId

    //付款金额
    BigDecimal fkje
    //付款日期
    Date fkrq

    //证件类型 0是身份证1是营业执照
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
