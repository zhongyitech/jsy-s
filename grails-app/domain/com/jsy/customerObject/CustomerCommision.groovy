package com.jsy.customerObject
/**
 * 客户分配
 */
class CustomerCommision {
    //客户类型
    int customerType
    //姓名
    String username
    //开户行
    String khh
    //银行账号
    String yhzh
    //本金提成比例
    double bjbl
    //利息比例
    double lxbl
    //国籍
    String gj
    //证件类型
    String zjlx
    //证件号码
    String zjhm


    static constraints = {
        gj nullable: true
    }
}
