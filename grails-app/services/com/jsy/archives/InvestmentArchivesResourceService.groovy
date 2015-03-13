package com.jsy.archives

import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.fundObject.RegisterContract
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray

class InvestmentArchivesResourceService {

    def create(InvestmentArchives dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = InvestmentArchives.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(InvestmentArchives.class, id)
        }
        obj
    }

    def readAll() {
        InvestmentArchives.findAll()
    }

    def update(InvestmentArchives dto, int id) {
        def obj = InvestmentArchives.get(id)
        obj?.uploadFiles.each {
            it.delete()
        }
        if (!obj) {
            throw new DomainObjectNotFoundException(InvestmentArchives.class, id)
        }
        obj.union(dto)
        obj.save(failOnError: true)
    }

    void delete(id) {
        def obj = InvestmentArchives.get(id)
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
        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
        def c = Customer.findAllByNameLike("%" +queryparam+"%")
        def d = InvestmentArchives.findAllByContractNumLike("%"+queryparam+"%")

        def page = InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [sort:"id", order:"desc",max: pagesize,  offset: startposition])
        def ja = new JSONArray()
        page.each {
            JSONObject JSOB =new JSONObject((it as JSON).toString());
            def cus=it.customer
            if (null == cus){
                JSOB.put("customer", "{}")
            }else {
                JSOB.put("customer", it.customer.properties)
            }
            ja.put(JSOB)
        }
        json.put("page", ja)
//        json.put("page", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [sort:"id", order:"desc",max: pagesize,  offset: startposition]))
        json.put("size", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c).size())

        return  json

    }

    def IAOutput(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
        def c = Customer.findAllByNameLike("%" +queryparam+"%")
        def d = InvestmentArchives.findAllByContractNumLike("%"+queryparam+"%")
        json.put("page", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [max: pagesize, sort:"id", order:"desc", offset: startposition]))
        json.put("size", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c).size())
        InvestmentArchives.listOrderByCustomer()
        return  json

    }

    def findByParm(String queryparam){
        if(null == queryparam){
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
//        InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrUsernameLike(f, "%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%")
        def ia = InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrUsernameLike(f, "%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%")
        return ia
    }
}
