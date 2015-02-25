package com.jsy.fundObject
/**
 * 基金提成分配范围
 */
class Tcfpfw {

    //部门经理
    int manageerId
    //业务提成
    double  businessCommision
    //管理提成
    double  manageCommision

    //是否包销
    boolean allSell=false
    //包销收益率
    double investment=0

    static constraints = {
    }
}
