package com.jsy.auth

import com.jsy.utility.DomainHelper
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class UserResourceService {

    def findByName(def username) {
        return User.findByUsername(username)
    }

    def create(User dto) {
        dto.save(failOnError: true)
        return dto
    }

    def read(id) {
        def obj = User.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(User.class, id)
        }
        obj
    }

    def readAll() {
        User.findAll()
    }

    /**
     * 更新用户的信息
     * @param dto 传递的参数
     * @param id 用户的ID
     * @roles 角色信息
     * @return 返回用户信息
     */
    def update(User dto, Long uid, List role) {
        def obj = User.get(uid)
        if (!obj) {
            throw new DomainObjectNotFoundException(User.class, dto.id)
            return false
        }
//        obj.union(dto)
//        obj.properties = dto.properties
        if (dto.password) {
            obj.password = dto.password
        }
        if (dto.chainName) {
            obj.chainName = dto.chainName
        }
        if (dto.skr) {
            obj.skr = dto.skr
        }
        if (dto.khh) {
            obj.khh = dto.khh
        }
        if (dto.yhzh) {
            obj.yhzh = dto.yhzh
        }
        if (null != dto.isUser || "" != dto.isUser) {
            obj.isUser = dto.isUser
        }
        if (dto.department) {
            obj.department = dto.department
        }
        if (null != dto.enabled || "" != dto.enabled) {
            obj.enabled = dto.enabled
        }
        if (null != dto.accountExpired || "" != dto.accountExpired) {
            obj.accountExpired = dto.accountExpired
        }
        if (null != dto.accountLocked || "" != dto.accountLocked) {
            obj.accountLocked = dto.accountLocked
        }
        if (null != dto.passwordExpired || "" != dto.passwordExpired) {
            obj.passwordExpired = dto.passwordExpired
        }
        //更新角色信息

        def ur = UserRole.findAllByUser(User.get(uid))
        if (ur) {
            ur.each {
                it.delete()
            }
        }

        print(role)
//        obj.save()
        role.each {
            if (!Role.exists(it)) {
                throw new Exception("ID:" + it + " Not found!")
            }
            print(uid)
            if (!UserRole.exists(uid, it)) {
                def r = Role.get(it)
                print(r)
                UserRole.create(obj, r)
            }
        }
        obj
    }

    def delete(Long id) {
        def obj = User.get(id)
        if (obj == null) return false
        obj.delete()
        return  true
    }

    def readAllForPage(def query) {

        def dc = DomainHelper.getDetachedCriteria(User, query)
        //todo: other code

        //按分页要求返回数据格式 [数据,总页数]
        return [data: dc.list([max: query.pagesize, offset: query.startposition]), total: pagesize == 0 ? 0 : dc.count()]
    }
}
