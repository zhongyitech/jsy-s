package com.jsy.archives

import com.jsy.bankConfig.BankAccount
import com.jsy.system.UploadFile

//投资档案中的客户信息(生成投资档案时的信息）
class CustomerArchives {
    transient springSecurityService
    def authorityService

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
    String zch='empty'

    //开户行
    String khh=""

    //联系电话
    String telephone

    //联系手机
    String phone

    //邮政编码
    String postalcode

    //通讯地址
    String callAddress


    //email
    String email

    //备注
    String remark

    //附件 & 银行账号
    static hasMany = [uploadFiles:UploadFile,bankAccount:BankAccount]
    static constraints = {
        fddbr nullable: true
        callAddress unllable: true
        email nullable:true
        remark nullable: true
        telephone  nullable: true
        postalcode  nullable: true
        zch unllable: true
        khh unllable:true
        credentialsAddr unllable:true
    }
    static mapping = {
        zch column: 'zch_me'
    }


    def beforeInsert(){
        //check operation
        def user = springSecurityService.getCurrentUser()
        if(user){//只能判断当前用户，对于空user的情况，有可能是在bootstrap中包的
            if(!authorityService.checkAuth("com.jsy.customerObject.Customer", "creat")){
                authorityService.throwError("insert","customer");
            }
        }

    }

    def beforeUpdate() {
        //check operation
        def user = springSecurityService.getCurrentUser()
        if(user){
            if(!authorityService.checkAuth("com.jsy.customerObject.Customer", "update")){
                authorityService.throwError("update","customer");
            }
        }

    }

    def beforeDelete() {
        //check operation
        def user = springSecurityService.getCurrentUser()
        if(user){
            if(!authorityService.checkAuth("com.jsy.customerObject.Customer", "delete")){
                authorityService.throwError("delete","customer");
            }
        }

    }
}
