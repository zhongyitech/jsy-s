package com.jsy.project
/**
 * 记录每个target变化的结果
 * 一旦付款，改变应收款数额
 *
 * 该记录不能更新，否则自己手动更新对应的payRecord
 */
class ShouldReceiveRecord {
    //是否删除
    boolean archive = false;

    int seq
    String target               // "original","firstyear","maintain","channel","overdue","penalty","borrow"
    BigDecimal amount           // 这个字段会一直变化，随着收款的情况

    //common
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
        payRecord: PayRecord,   // 付款
    ];

    static constraints = {

    }



}
