package com.jsy.archives

import com.jsy.auth.User
import com.jsy.fundObject.Fund

/**
 * 合同登记表
 */
class ContractRegister {
    def springSecurityService

    //基金名称
    Fund fund
    //登记人
    User djr
    //登记时间
    Date djsj
    //起始编号
    String qsbh
    //结束编号
    String jsbh
    //套数
    Long sum
    //备注
    String bz

    def beforeInsert() {
        this.djr=springSecurityService.getCurrentUser()
    }

    static constraints = {
        bz nullable: true
        djr nullable: true
        djsj nullable: true
    }
}
