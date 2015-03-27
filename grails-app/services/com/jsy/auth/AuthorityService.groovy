package com.jsy.auth

import grails.transaction.Transactional
import org.json.JSONArray
import org.json.JSONObject

@Transactional
class AuthorityService {
    def springSecurityService

    //根据权限过滤字段-多个过滤
    def getAuth(List list){
        String resourceName=list.get(0).class.toString()
        Resource resource=Resource.findByObjectName(resourceName)
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
        //检查查看权限
        if(!checkAuth(resourceRoles,'read')){
            return
        }
        Map map=new HashMap()
        resourceRoles.each {resourceRole->
            resourceRole.propertys.each {property->
                if(property.visible){
                    map.put(property.name,property.visible)
                }
            }
        }
        JSONArray jsonArray=new JSONArray()
        list.each {obj->
            def jsonObject=new JSONObject()
            obj.properties.each {
                if(map.containsKey(it.key)){
                    jsonObject.put(it.key,it.value)
                }else {
                    jsonObject.put(it.key,null)
                }
            }
            if(map.containsKey("id")){
                jsonObject.put("id",obj.id)
            }else {
                jsonObject.put("id",null)
            }
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }

    //根据权限过滤字段-单个过滤
    def getAuth(Object obj){
        String resourceName=obj.class.toString()
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(resourceName)
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
        //检查查看权限
        if(!checkAuth(resourceRoles,'read')){
            return
        }
        Map map=new HashMap()
        resourceRoles.each {resourceRole->
            resourceRole.propertys.each {property->
                if(property.visible){
                    map.put(property.name,property.visible)
                }
            }
        }
        def jsonObject=new JSONObject()
        obj.properties.each {
            if(map.containsKey(it.key)){
                jsonObject.put(it.key,it.value)
            }else {
                jsonObject.put(it.key,null)
            }
        }
        return jsonObject
    }

    //检查用户是否有权限操作
    def checkAuth(String resourceName,String cz){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(resourceName)
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
        resourceRoles.each {
            it.operations.each {operation->
                if(operation.cz==cz&&operation.visible){
                    return true
                }
            }
        }
        return false
    }

    //检测用户是否具有操作资源权限
    def checkAuth(List<ResourceRole> resourceRoles,String cz){
        boolean isOk = false;
        resourceRoles.each {
            it.operations.each {operation->
                if(operation.cz==cz&&operation.visible){
                    isOk =  true
                }
            }
        }
        return isOk
    }

    def serviceMethod() {
    }

}
