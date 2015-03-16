package com.jsy.auth

import org.codehaus.groovy.grails.web.json.JSONObject
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
     *更新用户的信息
     * @param dto   传递的参数
     * @param id    用户的ID
     * @roles       角色信息
     * @return      返回用户信息
     */
    def update(User dto, Long uid,List role) {
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
            if(!Role.exists(it)){
                throw new Exception("ID:"+it +" Not found!")
            }
            print(uid)
            if(!UserRole.exists(uid,it)){
                def r=Role.get(it)
                print(r)
                UserRole.create(obj,r)
            }
        }
        obj
    }

    def delete(def id) {
        def obj = User.get(id)
        if (obj) {
            obj.delete()
            return true
        } else {
            return false;
        }
    }

    def readAllForPage(Long pagesize, Long startposition, String queryparam) {
        JSONObject json = new JSONObject()
        if (null == queryparam) {

            queryparam = ""
        }
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        json.put("page", User.findAllByChainNameLikeOrUsernameLike("%" + queryparam + "%", "%" + queryparam + "%", [max: pagesize, sort: "id", order: "desc", offset: startposition]))
        json.put("size", User.findAllByChainNameLikeOrUsernameLike("%" + queryparam + "%", "%" + queryparam + "%").size())

        return json

    }

}
