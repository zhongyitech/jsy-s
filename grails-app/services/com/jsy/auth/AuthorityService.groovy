package com.jsy.auth

import grails.converters.JSON
import grails.transaction.Transactional
import org.json.JSONArray
import org.json.JSONObject

@Transactional(rollbackFor = Throwable.class)
class AuthorityService {
    def springSecurityService

    //根据权限过滤字段-多个过滤
    def getAuth(List list){
        String resourceName=list.get(0).class.toString()
        Resource resource=Resource.findByObjectName(resourceName)
        if(!resource){
            return list as JSON
        }
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
        Resource resource=Resource.findByObjectName(resourceName)
        if(!resource){
            return obj as JSON
        }
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
        return jsonObject
    }

    //检查用户是否有权限操作
    //resourceName: 资源名称：基金，档案，客户，用户
    //cz: 操作：read, creat, update, delete
    def checkAuth(String resourceName,String cz){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        Resource resource=Resource.findByObjectName(resourceName)
        def resourceRoles=ResourceRole.findAllByRoleInListAndResource(roles,resource)
        boolean b=false
        resourceRoles.each {
            it.operations.each {operation->
                if(operation.cz==cz&&operation.visible){
                    b=true
                }
            }
        }
        return b
    }

    def throwError(def operate, def domain){
        throw new Exception("Current User does not have the $operate right of $domain, operation deny!")
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

    //获取用户的操作权限
    def getOperationAuth(User user) {
        def operations = []
        def roles = UserRole.findAllByUser(user).collect{it.role}

        roles?.each {role ->
            def resources = ResourceRole.findAllByRole(role).collect{it.resource}
            resources.each {resource ->
                operations << resource.operations
            }
        }

        return operations?.unique { a, b -> a.id <=> b.id }

    }

    //获取用户某个业务模型的字段
    def getProperty(User user){
        def properties = []
        def roles = UserRole.findAllByUser(user).collect{it.role}

        roles?.each {role ->
            def resources = ResourceRole.findAllByRole(role).collect{it.resource}
            resources.each {resource ->
                properties << resource.propertys
            }
        }

        return properties?.unique { a, b -> a.id <=> b.id }
    }

    /**
     * 这是一个脏操作
     * @param obj
     * @param objectClassName
     * @param user
     * @return
     */
    def filterObjectProperty(def obj,def objectClassName){
        def user = springSecurityService.getCurrentUser()
        obj.discard()
        getUnVisibleProperty(user,objectClassName)?.each{prop->
            if(obj.hasProperty(prop.name)){
                encodeField(obj, prop.name)
            }
        }
        obj
    }

    /**
     * 这是一个脏操作
     * @param list
     * @param objectClassName
     * @param user
     * @return
     */
    def filterCollectionProperty(def list,def objectClassName){
        list.each{obj->
            filterObjectProperty(obj,objectClassName)
        }
        list
    }

    //获取用户某个业务模型的字段
    //domain: 资源类型 ： com.jsy.archives.InvestmentArchives
    def getUnVisibleProperty(User user, def domain){
        def properties = []
        def roles = UserRole.findAllByUser(user).collect{it.role}

        roles?.each {role ->
            def resourceRoles = ResourceRole.findAllByRole(role).findAll{it.resource.objectName==domain}
            resourceRoles.each {resourceRole ->
                resourceRole.propertys?.each{pp->
                    properties << pp
                }
            }
        }

        return properties?.unique { a, b -> a.id <=> b.id }
    }

    def encodeField(Object obj, String fieldName){
        if(fieldName=="id")return;
        if(obj."$fieldName" instanceof String){
            obj."$fieldName"="***"
        }else if(obj."$fieldName" instanceof BigDecimal){
            obj."$fieldName"=-1
        }else if(obj."$fieldName" instanceof BigDecimal){
            obj."$fieldName"=-1
        }else{
            obj."$fieldName"=null
        }

    }

}

















