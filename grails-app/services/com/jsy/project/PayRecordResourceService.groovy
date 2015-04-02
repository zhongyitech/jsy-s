package com.jsy.project

import com.jsy.fundObject.Fund
import com.jsy.util.OrderProperty
import com.jsy.util.SearchProperty
import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.codehaus.groovy.grails.web.json.JSONArray
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONObject

import java.text.SimpleDateFormat

class PayRecordResourceService {

    /**
     * 查询付款记录信息
     *
     * StopDate是一个假设性日期，如果没有则默认是查询截止至今天的数据情况
     * @param criteriaStr
     * @return
     */
    def readAllForPage(String criteriaStr){
        JSONObject obj = JSON.parse(criteriaStr)

        //get stop date
        Date stopDate = null
        if(obj.has("stopDate")){
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            stopDate = sf.parse(obj.stopDate)
        }

        def criterib = new DetachedCriteria(PayRecord).build {
            //and
            Object andObj = obj.get("and-prperties")
            JSONArray array = (JSONArray)andObj;
            array.each{property->
                SearchProperty p =new SearchProperty(property);
                if ("between".equals(property.get("operate"))) {
                    between(p.key,new Long(p.gapValue1),new Long(p.gapValue2))
                }else if(p.value && !"".equals(p.value) && "like".equals(property.get("operate"))){
                    like(p.key,"%"+p.value+"%")
                }else if(p.value && !"".equals(p.value) && "eq".equals(property.get("operate"))){
                    eq(p.key,Fund.get(p.value)) // TODO hard code here.
                }
            }

            //or
            Object orObj = obj.get("or-prperties")
            JSONArray array2 = (JSONArray)orObj;
            if(array2.size()>0){
                or {
                    array2.each{property->
                        SearchProperty p =new SearchProperty(property);
                        if ("between".equals(property.get("operate"))) {
                            between(p.key,new Long(p.gapValue1),new Long(p.gapValue2))
                        }else if(p.value && !"".equals(p.value) && "like".equals(property.get("operate"))){
                            like(p.key,"%"+p.value+"%")
                        }else if(p.value && !"".equals(p.value) && "eq".equals(property.get("operate"))){
                            eq(p.key,p.value)
                        }
                    }
                }
            }

            //orderby
            Object orderByObj = obj.get("orderby-prperties")
            JSONArray array3 = (JSONArray)orderByObj;
            if(array3.size()>0){
                or {
                    array3.each{property->
                        OrderProperty p =new OrderProperty(property);
                        order(p.key,p.value)
                    }
                }
            }
        }

        def params = [:]
        params.max = Math.min(obj.get("page").max?.toInteger() ?: 25, 100)
        params.offset = obj.get("page").offset ? obj.get("page").offset.toInteger() : 0

        def results = criterib.list(params)
        results = results.collect {payRecord->
            payRecord.getShowProperties(stopDate);
        }
        def total = criterib.size()

        def rtn = [:]
        rtn.results=results
        rtn.total=total
        return rtn
    }
    def create(PayRecord dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = PayRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PayRecord.class, id)
        }
        obj
    }

    def readAll() {
        PayRecord.findAll()
    }

    def update(PayRecord dto) {
        def obj = PayRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PayRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = PayRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
