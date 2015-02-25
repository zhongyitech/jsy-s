package com.jsy.archives

import com.jsy.auth.User
import com.jsy.fundObject.Fund
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class ContractRegisterResourceService {

    def create(ContractRegister dto) {
        //截取合同编号
        int qs=Integer.parseInt(dto.qsbh.substring(5))
        int js=Integer.parseInt(dto.jsbh.substring(5))
        String bh=dto.qsbh.substring(0,5)
        //循环新建合同
        for(int i=qs;i<=js;i++){
            def c = new Contract(htbh:bh+i,fund: dto.fund,djsj: dto.djsj)
            c.save(failOnError: true)
        }
        dto.save(failOnError: true)
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        def user = User.findAllByChainNameLike("%"+queryparam+"%")
        def fund = Fund.findAllByFundNameLike("%"+queryparam+"%")
        JSONObject json = new JSONObject()
        json.put("page", ContractRegister.findAllByFundInListOrDjrInList(fund, user,  [sort: "id",order: "desc",max: pagesize, offset: startposition]))
        json.put("size", ContractRegister.findAllByFundInListOrDjrInList(fund, user).size())

        return  json

    }

    def read(id) {
        def obj = ContractRegister.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ContractRegister.class, id)
        }
        obj
    }

    def readAll() {
        ContractRegister.findAll()
    }

    def update(ContractRegister dto) {
        def obj = ContractRegister.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ContractRegister.class, dto.id)
        }
        obj.union(dto)
        obj
    }

    void delete(id) {
        def obj = ContractRegister.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
