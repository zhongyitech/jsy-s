package com.jsy.bankConfig

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class PaymentRecordResourceService {

    def create(PaymentRecord dto) {
        dto.save()
    }

    def read(id) {
        def obj = PaymentRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PaymentRecord.class, id)
        }
        obj
    }

    def readAll() {
        PaymentRecord.findAll()
    }

    def update(PaymentRecord dto) {
        def obj = PaymentRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PaymentRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = PaymentRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()

        json.put("page", PaymentRecord.list(max: pagesize, offset: startposition))
        json.put("size", PaymentRecord.list().size())

        return  json

    }

}
