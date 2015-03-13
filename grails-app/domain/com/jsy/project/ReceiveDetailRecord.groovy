package com.jsy.project

import com.jsy.fundObject.Fund

/**
 * 收款明细，一汇款多项收款
 *
 * 该记录不能更新，否则自己手动更新对应的payRecord
 */
class ReceiveDetailRecord {

    String target // "original","firstyear","maintain","channel","overdue","penalty","borrow"
    BigDecimal amount

    //common
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
            receiveRecord: ReceiveRecord,     //收款
            payRecord: PayRecord,     //收款

    ];

    static constraints = {

    }


    def beforeInsert() {
        if(amount<0){
            throw new Exception("the amount is <0")
        }
        if("original".equals(target)){
            payRecord.payMainBack+=amount
        }
        payRecord.totalPayBack += amount
    }

}
