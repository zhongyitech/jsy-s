package com.jsy.system

import com.jsy.auth.User

/**
 * 公司表
 */
class Company {

    //公司名称
    String name
    //公司简称
    String aliasName
    //公司描述
    String description
    //成立日期
    Date buildDate
    //责任人
    User responsible
    //联系电话
    String phone
    //状态
    String status
    //备注信息
    String memo
    //所在省
    String province
    //所在市
    String city
    //所在地区
    String region
    //地址
    String address


    static constraints = {
    }
}
