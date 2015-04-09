package com.jsy.system

import com.jsy.auth.User
import com.jsy.fundObject.FundCompanyInformation

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

    //负责人
    User leader

    //职能
    TypeConfig performance

    //上级部门
    Department parent

    //公司
    FundCompanyInformation fundCompanyInformation

    //belongsTo = [company:Company]
    def beforeInsert() {
        if (!performance) {//默认值！
            performance = TypeConfig.findByTypeAndMapValue(8, 1)
        }
    }

    static constraints = {
        performance nullable: true
        description nullable: true
        memo nullable: true
        buildDate nullable: true
        type nullable: true
        status nullable: true
        fundCompanyInformation nullable: true
        leader nullable: true

        //为空表示是一级部门
        parent nullable: true
    }

    /**
     * 判断是否是一级部门
     * @return
     */
    public boolean isRootDepartment() {
        return parent==null
    }
}
