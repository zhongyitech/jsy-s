package com.jsy.bankConfig

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class BankTransactionsRecordResourceService {
    static final int MANAGE_TYPE_Processed=1
    //新的记录
    static final int MANAGE_TYPE_NEW=0
    //弃用的记录
    static final int MANAGE_TYPE_DEL=3

    def create(BankTransactionsRecord dto) {
        dto.save()
    }

    def read(id) {
        def obj = BankTransactionsRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankTransactionsRecord.class, id)
        }
        obj
    }

    def readAll() {
        BankTransactionsRecord.findAll()
    }

    def update(BankTransactionsRecord dto) {
        def obj = BankTransactionsRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankTransactionsRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = BankTransactionsRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }

    /**
     * 处理银行流水记录,生成记账凭证单
     */
    def getBankOrderList(){
        def data=BankTransactionsRecord.findAllByManaged(false)
        if(data.size()==0) return


    }

}
