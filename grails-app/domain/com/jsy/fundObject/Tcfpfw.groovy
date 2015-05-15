package com.jsy.fundObject
/**
 * 基金提成分配范围
 */
class Tcfpfw {

    //部门经理
    int manageerId
    //业务提成
    double businessCommision
    //管理提成
    double manageCommision

    //该部门的税率
    double rate

    //提成方式( true:税前/ false:税后
    boolean rateBefore = false
    //是否包销
    boolean allSell = false
    //包销收益率
    double investment = 0

    static constraints = {
    }
}
