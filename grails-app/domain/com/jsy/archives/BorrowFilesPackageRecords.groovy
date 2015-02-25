package com.jsy.archives

import com.jsy.auth.User

class BorrowFilesPackageRecords {

    FilePackage filePackage//文件包
    User user//借阅人
    Date borrowTime=new Date()//借阅时间
    Date shouldReturnTime//应该归还时间
    Boolean returned=false//是否已经归还
    Date returnTime//归还时间
    String remark//备注
    String returnRemark//备注

    static constraints = {
        returned nullable: true
        returnTime nullable: true
        remark nullable: true
        returnRemark nullable: true
    }
}
