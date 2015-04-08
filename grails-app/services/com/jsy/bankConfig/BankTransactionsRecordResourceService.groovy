package com.jsy.bankConfig

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class BankTransactionsRecordResourceService {

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
    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }

        json.put("page", BankTransactionsRecord.findAllByAccountNameLikeOrOtherSideNameLikeOrSummaryLikeOrTransactionsCodeLike("%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%","%"+queryparam+"%", [max: pagesize, offset: startposition]))
        json.put("size", BankTransactionsRecord.findAllByAccountNameLikeOrOtherSideNameLikeOrSummaryLikeOrTransactionsCodeLike("%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%","%"+queryparam+"%").size())

        return  json

    }
}
