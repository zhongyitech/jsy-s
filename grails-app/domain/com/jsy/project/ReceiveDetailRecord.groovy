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
            payRecord: PayRecord,             //付款

    ];

    static constraints = {

    }


    def beforeInsert() {
        if(amount<0){
            throw new Exception("the amount is <0")
        }
        if("original".equals(target)){
            payRecord.payMainBack+=amount
        }else if("firstyear".equals(target)){
            payRecord.interest_pay+=amount
        }else if("maintain".equals(target)){
            payRecord.manage_pay+=amount
        }else if("channel".equals(target)){
            payRecord.community_pay+=amount
        }else if("overdue".equals(target)){
            payRecord.overDue_pay+=amount
        }else if("penalty".equals(target)){
            payRecord.penalty_pay+=amount
        }else if("borrow".equals(target)){
            payRecord.borrow_pay+=amount
        }
        payRecord.totalPayBack += amount
    }

}
