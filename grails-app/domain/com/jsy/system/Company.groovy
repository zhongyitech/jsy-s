package com.jsy.system

import com.jsy.auth.User
import com.jsy.fundObject.Fund
import com.jsy.project.StockRight
import com.jsy.project.TSFlowFile
import com.jsy.project.TSWorkflow

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


    //执行商务合伙人
    String partner

    //法人国籍
    String partnerNationality

    //法人证件类型
    String docType

    //法人证件号码
    String docNumber

    //营业执照
    TSFlowFile businessLicense

    //组织机构代码证
    TSFlowFile orgCode

    //税务证件
    TSFlowFile taxFile

    //银行开户许可证
    TSFlowFile banksPermit

    //使用代码证
    TSFlowFile useCodePermit

    static hasMany = [
            othersFiles:TSFlowFile,  //其他文件
    ];

    static mappedBy = [
            businessLicense: "none",
            orgCode: "none",
            taxFile: "none",
            banksPermit: "none",
            useCodePermit: "none",
    ]

    static constraints = {
        partner nullable: true
        partnerNationality nullable: true
        docType nullable: true
        docNumber nullable: true

        businessLicense nullable: true
        orgCode nullable: true
        taxFile nullable: true
        banksPermit nullable: true
        useCodePermit nullable: true
    }
}
