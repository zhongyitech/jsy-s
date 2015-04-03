package com.jsy.auth

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class MenusRoleResourceService {
    def springSecurityService
    //获取角色有权限的菜单列表
    def getMenus(){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        def menus=MenusRole.findAllByRoleInListAndVisible(roles,true).collect {it.menus}.toSet().sort {it.id}
        def array=[]
        menus.each {
            if(it.parentId==0){
                JSONObject jsonObject =new JSONObject((it as JSON).toString());
                def childArray=[]
                menus.each {me->
                    if(me.parentId==it.id){
                        childArray.push(me.properties)
                    }
                }
                jsonObject.put("children",childArray)
                array.push(jsonObject)
            }
        }
        return array
    }

    def create(MenusRole dto) {
        dto.save()
    }

    def read(id) {
        def obj = MenusRole.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(MenusRole.class, id)
        }
        obj
    }

    def readAll() {
        MenusRole.findAll()
    }

    def update(MenusRole dto) {
        def obj = MenusRole.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(MenusRole.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = MenusRole.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
