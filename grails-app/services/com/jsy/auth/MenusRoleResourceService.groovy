package com.jsy.auth

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray

class MenusRoleResourceService {
    def springSecurityService
    //获取角色有权限的菜单列表
    def getMenus(){
        User user=springSecurityService.getCurrentUser()
        def roles=UserRole.findAllByUser(user).collect{it.role}
        def menus=MenusRole.findAllByRoleInListAndVisible(roles,true).collect {it.menus}.toSet().sort {it.id}
        JSONArray jsonArray=new JSONArray()
        menus.each {
            if(it.parentId==0){
                JSONObject jsonObject =new JSONObject((it as JSON).toString());
                JSONArray ja=new JSONArray()
                menus.each {me->
                    if(me.parentId==it.id){
                        ja.put(me.properties)
                    }
                }
                jsonObject.put("children",ja)
                jsonArray.put(jsonObject)
            }
        }
        return jsonArray.toString()
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
