package com.jsy.system

import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class OperationRecordResourceService {

    def create(OperationRecord dto) {
        dto.save()
    }

    def read(id) {
        def obj = OperationRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(OperationRecord.class, id)
        }
        obj
    }

    def readAll() {
        OperationRecord.findAll()
    }

    def update(OperationRecord dto) {
        def obj = OperationRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(OperationRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = OperationRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()
//        OperationRecord.findAllByCzrLikeOrParamsLikeOrUrlLike("%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%",[max: pagesize, offset: startposition])
        json.put("page", OperationRecord.findAllByCzrLikeOrParamsLikeOrUrlLike("%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%",[max: pagesize,sort: "id", order: "desc", offset: startposition]))
        json.put("size", OperationRecord.findAllByCzrLikeOrParamsLikeOrUrlLike("%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%").size())

        return  json

    }
}
