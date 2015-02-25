package com.jsy.fundObject


/**
 * 基金收益范围表
 */
class YieldRange {
    //对于合同类型
    String vers=""
    //最小起点收益
    BigDecimal investment1
    //最大收益
    BigDecimal investment2
    //收益率
    double yield
    //添加时间
    //Date addTime
    //添加人
    //User user
    //状态
    //int	status
    //优先级
    //String	level


    //static belongsTo = [fund:Fund]

    def beforeInsert() {
        if(investment2==null){
            investment2=0
        }
    }

    static constraints = {
        investment2 nullable: true
    }
}
