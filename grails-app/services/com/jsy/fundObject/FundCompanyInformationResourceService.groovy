package com.jsy.fundObject

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray

class FundCompanyInformationResourceService {

    def create(FundCompanyInformation dto) {
        dto.save(failOnError: true)
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

        def ja = new JSONArray()
        def page = FundCompanyInformation.list(max: pagesize, offset: startposition)
        page.each {
            JSONObject jsonObject =new JSONObject((it as JSON).toString());
            def pars = new JSONArray()
            it?.hhrpx?.split(",").each {fid->
                pars.put(FundCompanyInformation.get(Long.valueOf(fid)) as JSON)
            }
            jsonObject.put("partner", pars)
            ja.put(jsonObject)
        }

        json.put("page", ja)
        json.put("size", FundCompanyInformation.list().size())

        return  json

    }
}
