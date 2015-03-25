package com.jsy.auth

import grails.transaction.Transactional
import org.json.JSONArray
import org.json.JSONObject

@Transactional
class AuthorityService {
    def springSecurityService

    //根据权限过滤字段-多个过滤
    def getAuth(List list){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(list.get(0).class.toString())
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
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
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }

    //根据权限过滤字段-单个过滤
    def getAuth(Object obj){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(obj.class.toString())
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
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

    def serviceMethod() {
    }

}
