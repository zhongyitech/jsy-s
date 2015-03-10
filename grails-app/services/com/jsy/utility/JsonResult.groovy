package com.jsy.utility

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.validation.constraints.Null

/**
 * Created by lioa on 2015/3/10.
 * 封装返回的结果
 */
class JsonResult {
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    //返回成功的操作结果
    public static def success(/*结果*/def result,/*附加的消息*/String msg="OK"){
        def resmap=[:]
        resmap.rest_result=result
        resmap.rest_status=REST_STATUS_SUC
        resmap.rest_information=msg
        return resmap as JSON
    }

    public static def failed(String errorMsg,def result=null){
        def resmap=[:]
        resmap.rest_result=result
        resmap.rest_status=REST_STATUS_FAI
        resmap.rest_information=errorMsg
        return resmap as JSON
    }
}
