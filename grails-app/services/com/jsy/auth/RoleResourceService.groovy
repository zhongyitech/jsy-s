package com.jsy.auth

import com.jsy.utility.DomainHelper
import com.jsy.utility.MyException
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class RoleResourceService {

    def create(Role dto) {
//        dto.isDefault=true;
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
    def update(Role dto, /*需要更新的字段*/ map) {
        def obj = Role.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Role.class, dto.id)
        }
//        obj.properties = dto.properties
        obj.unionMap(map)
        obj
    }


    def delete(Long id) {
        def obj = Role.get(id)
        if(obj.isDefault){
            throw new MyException(obj.name + " 是预置角色，不允许删除！")
        }
        if (obj && !obj.isDefault) {
            obj.delete()
        }
        return true
    }

    def readAllForPage(def arg) {
        def dc = DomainHelper.getDetachedCriteria(Role, arg)
        return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
    }
}
