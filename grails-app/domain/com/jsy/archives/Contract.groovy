package com.jsy.archives

import com.jsy.fundObject.Fund

/**
 * 合同编号表
 * 作用1：记录印刷之后的合同，管理印刷件的使用情况
 * 作用2：创建投资档案的“合同编号”必须已经登记入表，从此处获取关联的基金对象
 */
class Contract {

    //合同编号
    String htbh
    //合同对于基金名称
    Fund fund
    //登记时间
    Date djsj=new Date()
    //是否领用
    boolean  sfly=false
    //领用时间
    Date lysj
    //归还时间
    Date ghsj
    //备注
    String bz

    //純数字编号
    int szbh

    def beforeInsert() {
        this.szbh=Integer.parseInt(htbh.substring(5))
    }

    static constraints = {
        djsj nullable: true
        lysj nullable: true
        ghsj nullable: true
        bz nullable: true
        szbh nullable: true
    }
}
