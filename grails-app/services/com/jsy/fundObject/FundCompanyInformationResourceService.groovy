package com.jsy.fundObject

import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class FundCompanyInformationResourceService {

    def create(FundCompanyInformation dto) {
        dto.save()
    }

    def read(id) {
        def obj = FundCompanyInformation.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FundCompanyInformation.class, id)
        }
        obj
    }

    def readAll() {
        FundCompanyInformation.findAll()
    }

    def update(FundCompanyInformation dto) {
        def obj = FundCompanyInformation.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FundCompanyInformation.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = FundCompanyInformation.get(id)
        if (obj) {
            obj.delete()
        }
    }
    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()

        json.put("page", FundCompanyInformation.list(max: pagesize, offset: startposition))
        json.put("size", FundCompanyInformation.list().size())

        return  json

    }
}
