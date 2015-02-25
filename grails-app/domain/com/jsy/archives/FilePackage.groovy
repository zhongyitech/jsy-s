package com.jsy.archives

import com.jsy.auth.User
import com.jsy.system.TypeConfig
import com.jsy.system.UploadFile

class FilePackage {
    def springSecurityService
        String fpname//档案包名称
        String fpno //档案包编号
        String fpcode //档案条码
        Date createdate //创建时间
        String contractNum//合同编号
        String contractNo //合同名称
        String fundName //基金名称
        String projectName //项目名称
        String signedPartner //签约方
        Date signedDate //签约日期
        User transfer //移交人
        Date transferDate //移交日期
        TypeConfig fptype //档案包类型
        String saveposition //档案存放位置 //档案室
        String cabinetno //档案柜编号
        String information //附加信息
        String description //描述
        TypeConfig borrowstatus //借阅状态
        String creator //创建者
//        String attachment //附件

        //附件
        static hasMany = [uploadFiles:UploadFile]
    def beforeInsert() {
        this.creator=springSecurityService.getCurrentUser()
        this.borrowstatus = TypeConfig.get(11)//11为在库
        this.createdate = new Date()
    }
    static constraints = {
        creator nullable: true
        borrowstatus nullable: true
        createdate nullable: true
        information nullable: true
    }
}
