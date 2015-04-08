package com.jsy.bankConfig

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class BankAccountResourceService {

    def create(BankAccount dto) {
        dto.save()
    }

    def read(id) {
        def obj = BankAccount.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankAccount.class, id)
        }
        obj
    }

    def readAll() {
        BankAccount.findAll()
    }

    def update(BankAccount dto) {
        def obj = BankAccount.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankAccount.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = BankAccount.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()

        json.put("page", BankAccount.list(max: pagesize, offset: startposition))
        json.put("size", BankAccount.list().size())

        return  json

    }

}
