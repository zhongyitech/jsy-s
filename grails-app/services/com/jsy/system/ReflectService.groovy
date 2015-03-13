package com.jsy.system

import com.jsy.utility.JsonResult
import grails.transaction.Transactional

@Transactional
class ReflectService {

    def getObject(String id, String className, String fields) {
        try {
            //不指定字段是否为空
            //if ("".equals(fields)) return JsonResult.success([:])
            def obj = Class.forName(className).newInstance().get(id)
            if(!obj)return JsonResult.success([:])
            def fieldArray = fields.split(",")
            def result = [:]
            fieldArray.each { field -> if(obj.metaClass.hasProperty(obj,field)) result[field] = obj[field]}
            return JsonResult.success(result.isEmpty() ? obj : result)
        } catch (Exception e) {
            return JsonResult.error(e.message)
        }
    }
}
