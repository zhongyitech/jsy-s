package com.jsy.customerObject

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class CustomerResourceService {

    def create(Customer dto) {
        dto.save()
    }

    def read(id) {
        def obj = Customer.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Customer.class, id)
        }
        obj
    }

    def readAll() {
        Customer.findAll()
    }

    def update(Customer dto) {
        def obj = Customer.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Customer.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Customer.get(id)
        if (obj) {
            obj.delete()
        }
    }
    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        //参数：pagesize 每页数据条数
        //      startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }
        def c = Customer.findAllByNameLike("%"+queryparam+"%", [max: pagesize, offset: startposition])
//        def dp = Department.findAllByDeptNameLike("%"+queryparam+"%")
//        def user = User.findAllByChinaNameLike("%"+queryparam+"%")
//        def fund = Fund.findAllByFundNameLike("%"+queryparam+"%")
//        print("dp.size="+dp.size())
//        print("user.size="+user.size())
//        print("fund.size="+fund.size())
//        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundNameInList(user, dp, fund, [max: pagesize, offset: startposition])
//        Customer.findAllByName("%"+queryparam+"%", [max: pagesize, offset: startposition])

        return  c
    }
}
