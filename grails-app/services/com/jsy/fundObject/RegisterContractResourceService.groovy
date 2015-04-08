package com.jsy.fundObject

import com.jsy.auth.User
import com.jsy.system.Department
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class RegisterContractResourceService {

    def create(RegisterContract dto) {
        print("SSSSSSSS")
        print(dto.receiveUser.id)
        print(dto.receiveUser.toString())
        print(dto.receiveDate.toString())


        return dto.save(failOnError: true)
    }

    def read(id) {
        def obj = RegisterContract.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(RegisterContract.class, id)
        }
        obj
    }

    def readAll() {
        RegisterContract.findAll()
    }
    def readReturnForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
        //参数：pagesize 每页数据条数
        //      startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }
//        pagesize = 10
//        startposition = 0
//        queryparam =
        def dp = Department.findAllByDeptNameLike("%"+queryparam+"%")
        def user = User.findAllByChainNameLike("%"+queryparam+"%")
        def fund = Fund.findAllByFundNameLike("%"+queryparam+"%")
//        RegisterContract tf = RegisterContract.findAllByActionType(false)
        print("dp.size="+dp.size())
        print("user.size="+user.size())
        print("fund.size="+fund.size())
//        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundNameInList(user, dp, fund, [max: pagesize, offset: startposition])
        def a = RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundInList(user, dp, fund, [max: pagesize,sort:"id", order:"desc", offset: startposition])
        def b = RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundInList(user, dp, fund)
        JSONArray c = new JSONArray()
        int d = 0
        a.each {
            if (true == it.actionType){
                c.put(it)
            }
        }
        json.put("page", c)
        b.each {
            if (true == it.actionType){
                d++
            }
        }
        json.put("size", d)

        return  json

    }

    def readUseForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
        //参数：pagesize 每页数据条数
        //      startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }

        def dp = Department.findAllByDeptNameLike("%"+queryparam+"%")
        def user = User.findAllByChainNameLike("%"+queryparam+"%")
        def fund = Fund.findAllByFundNameLike("%"+queryparam+"%")
//        RegisterContract tf = RegisterContract.findAllByActionType(false)
        print("dp.size="+dp.size())
        print("user.size="+user.size())
        print("fund.size="+fund.size())
//        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundNameInList(user, dp, fund, [max: pagesize, offset: startposition])
        def a = RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundInList(user, dp, fund, [max: pagesize,sort:"id", order:"desc", offset: startposition])
        def b = RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundInList(user, dp, fund)
        JSONArray c = new JSONArray()
        int d = 0
        a.each {
            if (false == it.actionType){
                print(it)
                c.put(it)
            }
        }
        json.put("page", c)
        b.each {
            if (false == it.actionType){
                d++
            }
        }
        json.put("size", d)

        return  json

    }



//    def findByParm(String queryparam){
//        if(null == queryparam){
//            queryparam = ""
//        }
//        def dp = Department.findAllByDeptNameLike("%"+queryparam+"%")
//        def user = User.findAllByChainNameLike("%"+queryparam+"%")
//        def fund = Fund.findAllByFundNameLike("%"+queryparam+"%")
//        print("dp.size="+dp.size())
//        print("user.size="+user.size())
//        print("fund.size="+fund.size())
////        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundNameInList(user, dp, fund, [max: pagesize, offset: startposition])
//        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundInList(user, dp, fund)
//    }

    def update(RegisterContract dto) {
        def obj = RegisterContract.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(RegisterContract.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = RegisterContract.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
