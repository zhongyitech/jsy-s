package com.jsy.utility

import com.jsy.auth.AuthorityService
import com.jsy.auth.User
import grails.converters.JSON

/**
 * Created by lioa on 2015/3/10.
 * 封装返回的结果
 */
class JsonResult {
    public static final String REST_STATUS_SUC = "suc"
    public static final String REST_STATUS_FAI = "err"

    /**
     * 封装成功的操作返回
     * @param result 返回的结果
     * @param total  分页后数据的总页数
     * @param msg
     * @return
     */
    //返回成功的操作结果
    public static JSON success(/*结果*/def result,def total=null,List<Map<String,Object>> list=null){
        def map=[:]
        map.rest_result=result
        if(total||total==0){
            map.rest_total=total
        }
        map.rest_status=REST_STATUS_SUC
        return filter(map,list)
    }

    /**
     * 封装出错的返回
     * @param errorMsg 出错提示消息
     * @param result 结果（错误的结果信息）
     * @return  JSON 对象
     */
    public static JSON error(String errorMsg="error",def result=null){
        def map=[:]
        map.rest_result=[msg:errorMsg,result:result]
        map.rest_status=REST_STATUS_FAI
        print(errorMsg)
        return map as JSON
    }

    def static filter(Object data,List<Map<String,Object>> list=[]){
        def json=new JSON(data)
        json.setExcludes(User.class,["password"])
        list.each {map->
            def values=(List)map.get("filter")
            def object=(Class)map.get("object")
            values=values==null?[]:values
            values.add("class")
            json.setExcludes(object,values)
        }
        return json
    }
}
