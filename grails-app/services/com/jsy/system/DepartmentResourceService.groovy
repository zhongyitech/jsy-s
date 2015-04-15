package com.jsy.system

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class DepartmentResourceService {

    def create(Department dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = Department.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Department.class, id)
        }
        obj
    }

    def readAll() {
        Department.findAll()
    }

    def update(Department dto) {
        def obj = Department.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Department.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Department.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
        if (null == queryparam){

            queryparam = ""
        }
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        json.put("page", Department.findAllByDeptNameLikeOrDescriptionLike( "%"+queryparam+"%", "%"+queryparam+"%", [max: pagesize,sort: "id",order: "desc" ,offset: startposition]))
        json.put("size", Department.findAllByDeptNameLikeOrDescriptionLike( "%"+queryparam+"%", "%"+queryparam+"%").size())

        return  json

    }

    /**
     * 获取所有的子级部门
     * @param dep
     */
    def getChild(Department dep){

        return Department.findAllByParent(dep)
    }
}
