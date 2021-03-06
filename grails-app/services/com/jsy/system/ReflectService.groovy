package com.jsy.system

import com.jsy.utility.JsonResult
import grails.transaction.Transactional

@Transactional(rollbackFor = Throwable.class)
class ReflectService {

    def getObject(String id, String className, String fields) {
        try {
            //不指定字段是否为空
            //if ("".equals(fields)) return JsonResult.success([:])
            def domain=Class.forName(className).newInstance()
            def ids=[]
            if(id&&id.contains(",")){
                id.split(',').each {
                    tmpId->
                        def tmp=castLong(tmpId)
                        if(tmp)ids.push(tmp)
                }
            }else{
                def tmpId=castLong(id)
                if(tmpId) ids.push(tmpId)
            }
            if(ids.isEmpty()) return JsonResult.success([])
            //less than 1000 id
            def obj= domain.withCriteria { inList('id',ids)}
            if(!obj) return JsonResult.success([])
            def result = []
            def fieldArray=fields.split(',')
            obj.each {
                item->
                    result.push(filterProp(item,fieldArray))
            }
            return JsonResult.success(result)
        } catch (Exception e) {
            return JsonResult.error(e.message)
        }
    }

    def castLong(String obj){
        try {
            return Long.parseLong(obj)
        }catch (Exception e){
            return null
        }
    }

    /**
     * 字段过滤
     * @param obj
     * @param fieldArray
     * @return
     */
    def filterProp(Object obj,String[] fieldArray){
        def result=[:]
        fieldArray.each {
            field ->
                if(obj.metaClass.hasProperty(obj,field))
                    result[field] = obj[field]
        }
        if(result.isEmpty()) return obj;
        if(!result.id&&obj.metaClass.hasProperty(obj,"id")) result["id"] = obj["id"]
        return result;
    }
}
