package com.jsy.project

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class ReceiveRecordResourceService {

    /**
     * 欢迎来到自动生成罚款的方法
     */
    def autoGenOverDateRecord(){
        PayRecord.list().each {payRecord->
            if(payRecord.isOverDate()){
                if(!payRecord.isGenOverShouldPay){//第一次
                    //逾期利息
                    new ShouldReceiveRecord(payRecord:payRecord,target:'overdue',amount:payRecord.getOverDue()).save(failOnError: true);

                    // 违约金
                    new ShouldReceiveRecord(payRecord:payRecord,target:'penalty',amount:payRecord.amount * payRecord.project.penalty_per).save(failOnError: true);

                    //马上就生成了
                    payRecord.isGenOverShouldPay = true
                    payRecord.save(failOnError: true)
                }else {//更新逾期利息
                    ShouldReceiveRecord shouldReceiveRecord = ShouldReceiveRecord.findByPayRecordAndTarget(payRecord, 'overdue');
                    shouldReceiveRecord.setAmount(payRecord.getOverDue())
                    shouldReceiveRecord.save(failOnError: true)
                }
            }

        }
    }

    def create(ReceiveRecord dto, def receiveDetails) {
        dto.save(failOnError: true)

        //创建receive detail
        receiveDetails?.each{detail->
            detail.receiveRecord=dto
            detail.save(failOnError: true)
        }
    }

    def read(id) {
        def obj = ReceiveRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ReceiveRecord.class, id)
        }
        obj
    }

    def readAll() {
        ReceiveRecord.findAll()
    }

    def update(ReceiveRecord dto) {
        def obj = ReceiveRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ReceiveRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ReceiveRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
