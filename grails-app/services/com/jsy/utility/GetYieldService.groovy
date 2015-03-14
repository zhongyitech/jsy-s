package com.jsy.utility

import com.jsy.fundObject.Fund
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject

@Transactional
class GetYieldService {

    def serviceMethod() {

    }
    public static def getYield(Long fundid,Long manageid, BigDecimal investment,String vers){
        //vers指合同版本，根据不同版本，得到不同的收益率
//        JSONObject json = new JSONObject()
        def obj=[:]
        Fund fund=Fund.get(fundid)
        fund.tcfpfw.each {
            if(it.manageerId==manageid){
//                json.put("rest_tc", it as JSON)
                obj.rest_tc=it
            }
        }
//            json.put("rest_yield", fund.getYieldRange(investment,vers))
        obj.rest_yield=fund.getYieldRange(investment,vers)

        return obj
    }
}
