package com.jsy.utility

import com.jsy.fundObject.Fund
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject

@Transactional(rollbackFor = Throwable.class)
class GetYieldService {

    def serviceMethod() {

    }
    /**
     * 获取年化收益率及提成比例信息
     * @param fundid
     * @param manageid
     * @param investment
     * @param vers
     * @return
     */
    public static def getYield(Long fundid,Long manageid, BigDecimal investment,String vers){
        //vers指合同版本，根据不同版本，得到不同的收益率
        def obj=[:]
        Fund fund=Fund.get(fundid)
        fund.tcfpfw.each {
            if(it.manageerId==manageid){
                obj.rest_tc=it
            }
        }
        obj.rest_yield=fund.getYieldRange(investment,vers)
        return obj
    }
}
