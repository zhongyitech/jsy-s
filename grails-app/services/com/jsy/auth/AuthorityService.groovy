package com.jsy.auth

import grails.transaction.Transactional
import org.json.JSONArray
import org.json.JSONObject

@Transactional
class AuthorityService {
    def springSecurityService

    def getAuth(List list,String resourceName){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(resourceName)
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
            JSONObject jsonObject=new JSONObject()
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

    def getAuth(Object obj,String resourceName){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(resourceName)
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
        Map map=new HashMap()
        resourceRoles.each {resourceRole->
            resourceRole.propertys.each {property->
                if(property.visible){
                    map.put(property.name,property.visible)
                }
            }
        }
        JSONObject jsonObject=new JSONObject()
        obj.properties.each {
            if(map.containsKey(it.key)){
                jsonObject.put(it.key,it.value)
            }else {
                jsonObject.put(it.key,null)
            }
        }
        return jsonObject
    }



    def serviceMethod() {
    }

}
