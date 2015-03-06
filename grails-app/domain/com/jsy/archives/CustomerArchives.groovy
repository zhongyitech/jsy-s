package com.jsy.archives

import com.jsy.system.UploadFile

//投资档案中的客户信息(生成投资档案时的信息）
class CustomerArchives {
    //客户名称
    String name

    //国家（地区）
    String country

    //证照类型
    String credentialsType

    //证照号码
    String credentialsNumber

    //身份证地址
    String credentialsAddr

    //法定代表人
    String fddbr

    //注册号
    String zch

    //开户行
    String khh

    //银行账号
    String yhzh

    //联系电话
    String telephone

    //联系手机
    String phone

    //邮政编码
    String postalcode

    //email
    String email

    //通讯地址
    String callAddress

    //备注
    String remark

    //附件
    static hasMany = [uploadFiles:UploadFile]
    static constraints = {
        email nullable:true
        remark nullable: true
    }
}
