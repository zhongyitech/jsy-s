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
        JSONObject json = new JSONObject()
//        double result = 0
//        boolean b=true
        Fund fund=Fund.get(fundid)
        fund.tcfpfw.each {
            if(it.manageerId==manageid){
//                if(it.allSell){
//                    result =  it.investment
//                    b=false
//                }
                json.put("rest_tc", it as JSON)
            }
        }
//        if (b){
            json.put("rest_yield", fund.getYieldRange(investment,vers))
//        }else {
//            return result
//            json.put("rest_yield",result)
//        }
        return json
    }
}
