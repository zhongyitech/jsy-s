package com.jsy.auth

import com.jsy.archives.InvestmentArchives
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import grails.converters.JSON
import grails.transaction.Transactional
import org.json.JSONArray
import org.json.JSONObject

/**
 * 权限设计
 * 新增修改删除：在用户/客户/档案/基金 domain中
 * 查询：meta编程，嵌入findAll,list,get
 * 字段过滤：具体的resource中处理，这里做门面工程
 *
 * 上述内容和本service是没有任何关系的，这里初始化权限模型中的数据，
 * 主要为管理员添加查询/新增/修改/删除操作的权限
 */
@Transactional(rollbackFor = Throwable.class)
class AuthInitService {
    def springSecurityService

    def init(){
        def admin = User.findByUsername('admin')
        def adminRole = Role.findByAuthority('ROLE_ADMIN')

        Resource jj = Resource.findByObjectName(Fund.class.getName());
        Resource da = Resource.findByObjectName(InvestmentArchives.class.getName());
        Resource kh = Resource.findByObjectName(Customer.class.getName());
        Resource yh = Resource.findByObjectName(User.class.getName());

        Operation operation1 = Operation.findByCz('read');
        Operation operation2 = Operation.findByCz('creat');
        Operation operation3 = Operation.findByCz('update');
        Operation operation4 = Operation.findByCz('delete');


        ResourceRole resourceRole1 = ResourceRole.findByRoleAndResource(adminRole,jj)?:new ResourceRole(role: adminRole, resource: jj).save(failOnError: true)

        ResourceRole resourceRole2 = ResourceRole.findByRoleAndResource(adminRole,da)?:new ResourceRole(role: adminRole, resource: da).save(failOnError: true)

        ResourceRole resourceRole3 = ResourceRole.findByRoleAndResource(adminRole,kh)?:new ResourceRole(role: adminRole, resource: kh).save(failOnError: true)

        ResourceRole resourceRole4 = ResourceRole.findByRoleAndResource(adminRole,yh)?:new ResourceRole(role: adminRole, resource: yh).save(failOnError: true)


        resourceRole1.addToOperations(operation1)
        resourceRole1.addToOperations(operation2)
        resourceRole2.addToOperations(operation1)
        resourceRole2.addToOperations(operation2)
        resourceRole3.addToOperations(operation1)
        resourceRole3.addToOperations(operation2)
        resourceRole4.addToOperations(operation1)
        resourceRole4.addToOperations(operation2)

        jj.propertys?.each {pro->
            resourceRole1.addToPropertys(pro)
        }
        da.propertys?.each {pro->
            resourceRole2.addToPropertys(pro)
        }
        kh.propertys?.each {pro->
            resourceRole3.addToPropertys(pro)
        }
        yh.propertys?.each {pro->
            resourceRole4.addToPropertys(pro)
        }

        resourceRole1.save(failOnError: true)
        resourceRole2.save(failOnError: true)
        resourceRole3.save(failOnError: true)
        resourceRole4.save(failOnError: true)

    }

}

















