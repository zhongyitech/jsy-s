package com.jsy.system
/**
 * 部门表
 */
class Department {

    //部门名称
    String deptName
    //部门描述
    String description
    //成立日期
    Date buildDate
    //部门类型
    int type
    //状态
    int status
    //备注
    String memo

    //belongsTo = [company:Company]

    static constraints = {
        description nullable: true
        memo nullable: true
    }
}
