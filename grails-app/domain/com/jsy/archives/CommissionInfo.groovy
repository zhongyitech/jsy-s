package com.jsy.archives

import com.jsy.system.ToDoTask
import com.jsy.system.TodoConfig

/**
 * 提成查询
 */
class CommissionInfo {

    //基金名称
    String fundName
    //客户名称
    String customer
    //投资金额
    BigDecimal tzje
    //投资收益率
    double syl
    //提成金额
    BigDecimal tcje
    //提成率
    double tcl
    //认购期限
    String rgqx
    //认购日期
    Date rgrq

    //对应的档案id
    Long archivesId
    //业务经理
    String ywjl
    //提成人
    String tcr=""
    //收款人
    String skr
    //开户行
    String khh
    //银行帐号
    String yhzh
    //是否公司
    boolean  sfgs
    //税率
    double sl
    //税前还是税后
    boolean  sqsh
    //是否已付税额
    boolean sfyfse
    //发票额
    BigDecimal fpje=0
    //付款金额
    BigDecimal fkje=0
    //税金
    BigDecimal sj=0
    //id
    Long comId=0

    //类型0为业务，1为管理
    int lx
    /**
     * 递交给OA的状态
     * 0：未提成申请
     * 1:为已经提交到oa，提成申请
     * 2:申请已处理
     */
    int type=0

    //支付时间
    Date zfsj

    //对应待办任务id
    Long todoId

    /**common字段**/
    Date dateCreated
    Date lastUpdated

    def beforeInsert() {
        TodoConfig todoConfig=TodoConfig.findByMkbs(1)
        ToDoTask toDoTask=ToDoTask.create(todoConfig.mkmc,todoConfig.cljs,todoConfig.url)
        zfsj=new Date()
        todoId=toDoTask.id
    }

    static constraints = {
        todoId nullable: true
        zfsj nullable: true
    }
}
