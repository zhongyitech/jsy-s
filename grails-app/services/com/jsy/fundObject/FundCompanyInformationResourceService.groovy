package com.jsy.fundObject

import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray

@Transactional(rollbackFor = Throwable.class)
class FundCompanyInformationResourceService {

    FundCompanyInformation create(FundCompanyInformation dto) {
        def obj = dto.save(failOnError: true)
        obj.bankAccount.each {
            it.companyInformation = obj
        }
        dto.partner.each {
            obj.addToPartner(it)
        }
        obj
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

    /**
     * 更新公司信息
     * @param dto
     * @return
     */
    def update(FundCompanyInformation dto) {
        def obj = FundCompanyInformation.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FundCompanyInformation.class, dto.id)
        }
        obj.properties = dto.properties
        obj.bankAccount.each {
            it.companyInformation = obj
            it.save(failOnError: true)
        }
        obj.partner.removeAll()
        dto.partner.each {
            println(it.companyName)
            obj.addToPartner(it)
        }
        obj.save(failOnError: true)
        obj
    }

    void delete(id) {
        def obj = FundCompanyInformation.get(id)
        if (obj) {
            obj.delete()
        }
    }
    /**
     * 获取公司的列表信息
     * @param pagesize
     * @param startposition
     * @param queryparam
     * @return
     */
    def readAllForPage(Long pagesize, Long startposition, String queryparam) {
        if (null == queryparam) {
            queryparam = ""
        }
        JSONObject json = new JSONObject()

        def ja = new JSONArray()
        def page = FundCompanyInformation.list(max: pagesize, offset: startposition)
        page.each {
            JSONObject jsonObject = new JSONObject((it as JSON).toString());
            def pars = new JSONArray()
            it?.hhrpx?.split(",").each { fid ->
                pars.put(FundCompanyInformation.get(Long.valueOf(fid)) as JSON)
            }
            jsonObject.put("partner", pars)
            ja.put(jsonObject)
        }

        json.put("page", ja)
        json.put("size", FundCompanyInformation.list().size())

        return json

    }
}
