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

    def update(Role dto) {
        def obj = Role.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, dto.id)
        }
//        obj.properties = dto.properties
        obj.union(dto)
        obj
    }

    //更新对象的值
    def update(Role dto , /*需要更新的字段*/map) {
        def obj = Role.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, dto.id)
        }
//        obj.properties = dto.properties
        obj.unionMap(map)
        obj
    }


    void delete(id) {
        def obj = Role.get(id)
        if (obj) {
            obj.delete()
        }
    }
    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()

        json.put("page", Role.findAllByNameLikeOrAuthorityLike("%" + queryparam + "%","%" + queryparam + "%",max: pagesize, offset: startposition))
        json.put("size", Role.findAllByNameLikeOrAuthorityLike("%" + queryparam + "%","%" + queryparam + "%").size())

        return  json

    }
}
