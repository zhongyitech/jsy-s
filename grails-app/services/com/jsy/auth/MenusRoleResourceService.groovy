package com.jsy.auth

import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
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

    /**
     * 获取系统菜单列表
     */
    def getMenuList(Long userRoleId){
        def role=Role.get(userRoleId)
        def menus=Menus.findAll()
        def menuList=MenusRole.findAllByRoleAndVisible(role,true).collect {it.menus}.collect {it.id}
        def array=[]
        def mapArray=[:]
        menus.each {
            def parentId=it.parentId
            def object=[id:it.id,checked:menuList.contains(it.id)]
            object.putAll(it.properties)
            if(parentId==0){
                array.add(object)
            }else {
                if(!mapArray[parentId]) mapArray[parentId]=[]
                mapArray[parentId].add(object)
            }
        }
        array.each {it.children=mapArray[it.id]}
        return array
    }

    def updateMenuRole(String data,Long roleId){
        def array = JSON.parse(data)
        MenusRole.executeUpdate("delete MenusRole where role.id = :roleId", [roleId:roleId])
        array.each {
            it.visible=true
            it.asType(MenusRole.class).save(failOnError: true)
        }
    }

    def create(MenusRole dto) {
        dto.save()
    }

    def read(Long id) {
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
