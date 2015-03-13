package com.jsy.utility

import grails.converters.JSON

/**
 * Created by lioa on 2015/3/10.
 * 封装返回的结果
 */
class JsonResult {
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    //返回成功的操作结果
    public static def success(/*结果*/def result,/*附加的消息*/String msg="OK",def page=null){
        def resmap=[:]
        resmap.rest_result=result
        if(page){
            resmap.rest_page=page
        }
        resmap.rest_status=REST_STATUS_SUC
        return resmap as JSON
    }

    /**
     * 封装出错的返回
     * @param errorMsg 出错提示消息
     * @param result 结果（错误的结果信息）
     * @return  JSON 对象
     */
    public static def error(String errorMsg,def result=null){
        def resmap=[:]
        resmap.rest_result=[msg:errorMsg,result:result]
        resmap.rest_status=REST_STATUS_FAI
        return resmap as JSON
    }
}
