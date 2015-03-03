package com.jsy.auth

import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class RoleResourceService {

    def create(Role dto) {
        dto.save()
    }

    def read(id) {
        def obj = Role.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, id)
        }
        obj
    }

    def readAll() {
        Role.findAll()
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
        if (null == queryparam){

            queryparam = ""
        }
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        json.put("page", Role.findAllByNameLike( "%"+queryparam+"%", [max: pagesize,sort: "id",order: "desc" ,offset: startposition]))
        json.put("size", Role.findAllByNameLike( "%"+queryparam+"%").size())

        return  json

    }

    def update(Role dto) {
        def obj = Role.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    def update(Role dto, def id) {
        def obj = Role.get(id)
        obj.name = dto.name
        obj.authority = dto.authority
        obj.save(failOnError: true)
        obj
    }

    void delete(id) {
        def obj = Role.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
