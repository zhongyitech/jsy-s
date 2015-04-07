package com.jsy.project

import com.jsy.bankConfig.BankAccount
import com.jsy.fundObject.Fund
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONObject

@Transactional(rollbackFor = Throwable.class)
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

    def create(JSONObject obj) {
        //基础数据转换
        Fund fund = Fund.get(obj.fundid)
        TSProject project = TSProject.get(obj.projectid)
        BankAccount bankAccount = BankAccount.get(obj.bankid)
        def paydate = Date.parse("yyyy-MM-dd", obj.paydate)


        def paytotal = obj.paytotal
        def remain_money_suggest = new BigDecimal(obj.remain_money_suggest)


        //根据前台的计算结果，进行再次验证，check same suggest： remain_money_suggest
        def _paytotal = new BigDecimal(obj.paytotal) + bankAccount.overReceive
        def receiveDetails = []

        if(obj.receiveDetail_struct){
            _paytotal = consumePayRecords(_paytotal, obj.receiveDetail_struct, receiveDetails);
        }

        if(_paytotal!=remain_money_suggest && Math.abs(_paytotal-remain_money_suggest)>1){
            throw new Exception("error remain count $_paytotal vs front $remain_money_suggest")
        }

        //数据保存
        if(remain_money_suggest>0){
            bankAccount.overReceive = remain_money_suggest
        }else{
            bankAccount.overReceive = 0
        }
        bankAccount.save(failOnError: true)

        ReceiveRecord dto = new ReceiveRecord(receiveDate:paydate,amount:paytotal,
                project:project,fund:fund,bankAccount:bankAccount,remain_charge:remain_money_suggest);
        dto.save(failOnError: true)

        //创建receive detail
        receiveDetails?.each{detail->
            detail.receiveRecord=dto
            detail.save(failOnError: true)
        }

        return dto
    }

    def consumePayRecords(_paytotal, shouldReceiveIds, receiveDetails){

        //target的顺序很重要呢， 保证顺序，dirty operate!!!
        def shouldReceives = []
        shouldReceiveIds.each { receiveId ->
            shouldReceives.push(ShouldReceiveRecord.get(receiveId));
        }
        shouldReceives?.sort { targetA,targetB->
            return -targetA.seq.compareTo(targetB.seq)
        }

        shouldReceives.each{shouldReceiveRecord->

            //处理一条应收记录
            if(shouldReceiveRecord){
                def payRecord = shouldReceiveRecord.payRecord

                if("original".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def temp = shouldReceiveRecord.amount   //临时保存一下 shouldReceiveRecord.amount ,因为这个值在过程中会改变

                    if(_paytotal>shouldReceiveRecord.amount){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: shouldReceiveRecord.amount,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        //钱用了
                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)
                    }else if(_paytotal>0){//少量余额
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)
                        payRecord.payMainBack = _paytotal
                    }
                    _paytotal= _paytotal - temp

                }else if("firstyear".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def temp = shouldReceiveRecord.amount   //临时保存一下 shouldReceiveRecord.amount ,因为这个值在过程中会改变

                    if(_paytotal>shouldReceiveRecord.amount){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: shouldReceiveRecord.amount,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)

                    }else if(_paytotal>0){
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)
                    }
                    _paytotal= _paytotal - temp

                }else if("maintain".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def temp = shouldReceiveRecord.amount   //临时保存一下 shouldReceiveRecord.amount ,因为这个值在过程中会改变

                    if(_paytotal>shouldReceiveRecord.amount){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: shouldReceiveRecord.amount,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)
                    }else if(_paytotal>0){
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                    }
                    _paytotal= _paytotal - temp


                }else if("channel".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def temp = shouldReceiveRecord.amount   //临时保存一下 shouldReceiveRecord.amount ,因为这个值在过程中会改变

                    if(_paytotal>shouldReceiveRecord.amount){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: shouldReceiveRecord.amount,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)
                    }else if(_paytotal>0){
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)
                    }
                    _paytotal= _paytotal - temp
                }else if("borrow".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def temp = shouldReceiveRecord.amount   //临时保存一下 shouldReceiveRecord.amount ,因为这个值在过程中会改变

                    if(_paytotal>shouldReceiveRecord.amount){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: shouldReceiveRecord.amount,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)
                    }else if(_paytotal>0){
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)
                    }
                    _paytotal= _paytotal - temp
                }else if("penalty".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def temp = shouldReceiveRecord.amount   //临时保存一下 shouldReceiveRecord.amount ,因为这个值在过程中会改变

                    if(_paytotal>shouldReceiveRecord.amount){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: shouldReceiveRecord.amount,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)
                    }else if(_paytotal>0){
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)
                    }
                    _paytotal= _paytotal - temp
                }else if("overdue".equals(shouldReceiveRecord.target)){
                    ReceiveDetailRecord detailRecord
                    def dueMoney = payRecord.getOverDue()

                    if(_paytotal>dueMoney){//有多余的钱
                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: dueMoney,payRecord:payRecord);
                        receiveDetails.push(detailRecord)

                        shouldReceiveRecord.amount = 0
                        shouldReceiveRecord.save(failOnError: true)
                    }else if(_paytotal>0){
                        shouldReceiveRecord.amount = shouldReceiveRecord.amount-_paytotal
                        shouldReceiveRecord.save(failOnError: true)

                        detailRecord = new ReceiveDetailRecord(target: shouldReceiveRecord.target, amount: _paytotal,payRecord:payRecord);
                        receiveDetails.push(detailRecord)
                    }
                    _paytotal= _paytotal - dueMoney
                }
            }
        }


        return _paytotal;
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
