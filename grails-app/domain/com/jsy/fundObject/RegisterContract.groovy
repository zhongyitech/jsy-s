package com.jsy.fundObject

import com.jsy.auth.User
import com.jsy.system.Department

/**
 * 合同领用归还表
 */
class RegisterContract {

    //序号
    String indexNum
    //领用部门
    Department department
    //领用人
    User receiveUser
    //领用时间
    Date receiveDate
    //基金
    Fund fund
    //起始编号
    String startNum
    //结束编号
    String endNum
    //套数
    int total

    //操作类型 false为领用 true为归还
    boolean actionType

    static constraints = {
    }
}
