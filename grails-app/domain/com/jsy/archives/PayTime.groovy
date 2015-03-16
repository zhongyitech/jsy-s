package com.jsy.archives
/**
 * 付息时间
 */
class PayTime {
    //排序
    int px
    //付息时间
    Date fxsj
    //是否已付息
    boolean sffx

    static belongsTo = [investmentArchives:InvestmentArchives]

    static constraints = {
    }
}
