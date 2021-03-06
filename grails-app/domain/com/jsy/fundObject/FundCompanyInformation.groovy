package com.jsy.fundObject

import com.jsy.bankConfig.BankAccount
import com.jsy.project.TSFlowFile
import com.jsy.system.Company
import com.jsy.system.Department
import com.jsy.system.TypeConfig
import com.jsy.system.UploadFile

//基金公司信息表
class FundCompanyInformation {
    //公司名称
    String companyName

    //项目
    //未知

    //法人代表
    String corporate

    //注册地址
    String address

    //营业额执照号码
    String credentialsNumber=""
    //募集账户
//    static hasMany = []

    //省
    String province

    //市
    String city

    //县
    String area

    //总公司
    FundCompanyInformation groupCompany

    //联系电话
    String telephone

    //公司简称
    String companyNickName

    //公司描述
    String companyDescription

    //成立日期
    Date foundingDate

    //责任人
    String responsiblePerson

    //传真
    String fax

    //状态
    String status

    //备注
    String remark

    //公司类型 0普通公司  1有限合伙
    TypeConfig companyType

    //协议模板 0为单GP   1为双GP
    int protocolTemplate = 0

    //公司电子印章
    UploadFile reportTokenImg

    //合伙人排序,id以逗号分开  1,2,3
    String hhrpx

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

    //是否是执行事务合伙人
    boolean isDefaultPartner=false

    //合伙人
    static hasMany = [
            partner    : FundCompanyInformation,
            bankAccount: BankAccount,
            othersFiles: TSFlowFile,  //其他文件
            funds      : Fund    //基金
    ]

    static mappedBy = [
            businessLicense: "none",
            orgCode        : "none",
            taxFile        : "none",
            banksPermit    : "none",
            useCodePermit  : "none",
    ]

    static constraints = {
        hhrpx nullable: true
//        companyType nullable: true
//        companyName nullable: true
        corporate nullable: true
        address nullable: true
        province nullable: true
        city nullable: true
        area nullable: true
        groupCompany nullable: true
        telephone nullable: true
//        companyNickName nullable: true
        companyDescription nullable: true
        foundingDate nullable: true
        responsiblePerson nullable: true
        fax nullable: true
        status nullable: true
        remark nullable: true
        companyType nullable: true
        protocolTemplate nullable: true

        businessLicense nullable: true
        orgCode nullable: true
        taxFile nullable: true
        banksPermit nullable: true
        useCodePermit nullable: true

        reportTokenImg nullable: true
    }
}
