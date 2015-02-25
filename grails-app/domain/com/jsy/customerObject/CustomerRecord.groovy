package com.jsy.customerObject

import com.jsy.auth.User

/**
 * 客户修改记录
 */
class CustomerRecord {
    def springSecurityService

    //对应客户id
    Long customerId
    //操作人
    User czr
    //操作时间
    Date czsj

    //客户名称
    String name
    //国家（地区）
    String country
    //证照类型
    String credentialsType
    //证照号码
    String credentialsNumber
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

    def beforeInsert() {
        czr=springSecurityService.getCurrentUser()
        czsj=new Date()
    }

    static constraints = {
        czr nullable: true
        czsj nullable: true
        name nullable: true
        country nullable: true
        credentialsType nullable: true
        credentialsNumber nullable: true
        khh nullable: true
        yhzh nullable: true
        phone nullable: true

        telephone nullable: true
        postalcode nullable: true
        callAddress nullable: true
        fddbr nullable: true
        zch nullable: true
        email nullable:true
    }

}
