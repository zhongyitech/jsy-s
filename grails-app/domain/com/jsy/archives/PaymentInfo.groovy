package com.jsy.archives

import com.jsy.system.ToDoTask
import com.jsy.system.TodoConfig

/**
 * 兑付查询
 */
class PaymentInfo {

    //基金名称
    String fundName
    //合同编号
    String htbh
    //客户名称
    String customerName
    //投资金额
    BigDecimal tzje
    //投资期限
    String tzqx
    //收益率
    double syl
    //应付利息
    BigDecimal yflx
    //应付本金
    BigDecimal yfbj
    //总计
    BigDecimal zj
    //开户行
    String khh
    //账号
    String yhzh
    //国籍
    String gj
    //证件类型
    String zjlx
    //证件号码
    String zjhm
    //部门经理
    String bmjl

    //对应的档案id
    Long archivesId
    //付息生产时间
    Date fxsj

    //兑付状态，true:已兑付，false:未兑付
    boolean  isAllow=false
    //递交给OA的状态，0：未申请，1：已申请，2：申请处理了
    int type=0

    //备注
    String bz=""

    //客户附件
//    String uuid

    //支付时间
    Date zfsj=new Date()
    //对应待办任务id
    Long todoId


    //common
    Date dateCreated
    Date lastUpdated


    def beforeInsert() {
        TodoConfig todoConfig=TodoConfig.findByMkbs(2)
        ToDoTask toDoTask=ToDoTask.create(todoConfig.mkmc,todoConfig.cljs,todoConfig.url)
        zfsj=new Date()
        todoId=toDoTask.id
    }

    static constraints = {
        zfsj nullable: true
        todoId nullable: true
    }
}
