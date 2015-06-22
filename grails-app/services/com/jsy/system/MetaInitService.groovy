package com.jsy.system

import com.jsy.archives.CustomerArchives
import com.jsy.archives.InvestmentArchives
import com.jsy.auth.User
import com.jsy.fundObject.Fund
import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray
import org.json.JSONObject

@Transactional(rollbackFor = Throwable.class)
class MetaInitService {
    def springSecurityService
    def authorityService

    def init() {
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy/MM/dd HH:mm:ss")
        }
        println "init_metaExtend start"
        Object.metaClass.union = { obj ->
            obj.properties.each { prop, val ->
                if (prop in ["metaClass", "class"]) return
                if (delegate.delegate.hasProperty(prop) && val) {
                    if (delegate.delegate.hasProperty(prop).setter) {
                        delegate.delegate[prop] = val
                    } else {
                    }
                }
            }
        }

        Object.metaClass.unionMap = { map ->
            map.each {
                if (delegate.delegate.hasProperty(it.key)) {
                    if (delegate.delegate.hasProperty(it.key).setter) {
                        delegate.delegate[it.key] = it.value
                    } else {

                    }
                }
            }
        }

        initFundSearchCheck();
        initInvestmentSearchCheck();
        initUserSearchCheck();
        initCustomerSearchCheck();


    }

    /**
     * 查询只检查是否有operation权限，不过滤字段
     * 查询只检查有限的：findAll,list,get,不检查findBy**
     * @return
     */
    def initFundSearchCheck(){
        // findAll
        def findAllGorm = Fund.metaClass.getMetaMethod('findAll')
        Fund.metaClass.static.findAll = { ->
            checkFundRead();
            return findAllGorm.invoke(delegate)
        }

        // list
        def listGorm = Fund.metaClass.getMetaMethod('list')
        Fund.metaClass.static.list = {
            checkFundRead();
            return listGorm.invoke(delegate)
        }
        Fund.metaClass.static.list = { LinkedHashMap p ->
            checkFundRead();
            return listGorm.invoke(delegate)
        }
        Fund.metaClass.static.list = { Map p ->
            checkFundRead();
            return listGorm.invoke(delegate)
        }

        // get
        def getGorm = Fund.metaClass.getMetaMethod('get')
        Fund.metaClass.static.get = { Integer id ->
            checkFundRead();
            return getGorm.invoke(delegate,id)
        }
        Fund.metaClass.static.get = { int id ->
            checkFundRead();
            return getGorm.invoke(delegate,id)
        }
        Fund.metaClass.static.get = { Long id ->
            checkFundRead();
            return getGorm.invoke(delegate,id)
        }

    }

    def initInvestmentSearchCheck(){
        // findAll
        def findAllGorm = InvestmentArchives.metaClass.getMetaMethod('findAll')
        InvestmentArchives.metaClass.static.findAll = { ->
            checkArchiveRead();
            return findAllGorm.invoke(delegate)
        }

        // list
        def listGorm = InvestmentArchives.metaClass.getMetaMethod('list')
        InvestmentArchives.metaClass.static.list = {
            checkArchiveRead();
            return listGorm.invoke(delegate)
        }
        InvestmentArchives.metaClass.static.list = { LinkedHashMap p ->
            checkArchiveRead();
            return listGorm.invoke(delegate)
        }
        InvestmentArchives.metaClass.static.list = { Map p ->
            checkArchiveRead();
            return listGorm.invoke(delegate)
        }

        // get
        def getGorm = InvestmentArchives.metaClass.getMetaMethod('get')
        InvestmentArchives.metaClass.static.get = { Integer id ->
            checkArchiveRead();
            return getGorm.invoke(delegate,id)
        }
        InvestmentArchives.metaClass.static.get = { int id ->
            checkArchiveRead();
            return getGorm.invoke(delegate,id)
        }
        InvestmentArchives.metaClass.static.get = { Long id ->
            checkArchiveRead();
            return getGorm.invoke(delegate,id)
        }
    }

    def initUserSearchCheck(){
        // findAll
        def findAllGorm = User.metaClass.getMetaMethod('findAll')
        User.metaClass.static.findAll = { ->
            checkUserRead();
            return findAllGorm.invoke(delegate)
        }

        // list
        def listGorm = User.metaClass.getMetaMethod('list')
        User.metaClass.static.list = {
            checkUserRead();
            return listGorm.invoke(delegate)
        }
        User.metaClass.static.list = { LinkedHashMap p ->
            checkUserRead();
            return listGorm.invoke(delegate)
        }
        User.metaClass.static.list = { Map p ->
            checkUserRead();
            return listGorm.invoke(delegate)
        }

        // get
        def getGorm = User.metaClass.getMetaMethod('get')
        User.metaClass.static.get = { Integer id ->
            checkUserRead();
            return getGorm.invoke(delegate,id)
        }
        User.metaClass.static.get = { int id ->
            checkUserRead();
            return getGorm.invoke(delegate,id)
        }
        User.metaClass.static.get = { Long id ->
            checkUserRead();
            return getGorm.invoke(delegate,id)
        }
    }

    def initCustomerSearchCheck(){
        // findAll
        def findAllGorm = CustomerArchives.metaClass.getMetaMethod('findAll')
        CustomerArchives.metaClass.static.findAll = { ->
            checkCustomerRead();
            return findAllGorm.invoke(delegate)
        }

        // list
        def listGorm = CustomerArchives.metaClass.getMetaMethod('list')
        CustomerArchives.metaClass.static.list = {
            checkCustomerRead();
            return listGorm.invoke(delegate)
        }
        CustomerArchives.metaClass.static.list = { LinkedHashMap p ->
            checkCustomerRead();
            return listGorm.invoke(delegate)
        }
        CustomerArchives.metaClass.static.list = { Map p ->
            checkCustomerRead();
            return listGorm.invoke(delegate)
        }

        // get
        def getGorm = CustomerArchives.metaClass.getMetaMethod('get')
        CustomerArchives.metaClass.static.get = { Integer id ->
            checkCustomerRead();
            return getGorm.invoke(delegate,id)
        }
        CustomerArchives.metaClass.static.get = { int id ->
            checkCustomerRead();
            return getGorm.invoke(delegate,id)
        }
        CustomerArchives.metaClass.static.get = { Long id ->
            checkCustomerRead();
            return getGorm.invoke(delegate,id)
        }
    }

    private void checkFundRead(){
        def user = springSecurityService.getCurrentUser()
        if(user){
            if(!authorityService.checkAuth("com.jsy.fundObject.Fund", "read")){
                authorityService.throwError("read","fund")
            }
        }
    }

    private void checkCustomerRead(){
        def user = springSecurityService.getCurrentUser()
        if(user){
            if(!authorityService.checkAuth("com.jsy.customerObject.Customer", "read")){
                authorityService.throwError("read","customer")
            }
        }
    }

    /**
     * 不能有这个检查，否则会进入循环检查
     */
    private void checkUserRead(){
//        def user = springSecurityService.getCurrentUser()
//        if(user){
//            if(!authorityService.checkAuth("com.jsy.auth.User", "read")){
//                authorityService.throwError("read","user")
//            }
//        }
    }

    private void checkArchiveRead(){
        def user = springSecurityService.getCurrentUser()
        if(user){
            if(!authorityService.checkAuth("com.jsy.archives.InvestmentArchives", "read")){
                authorityService.throwError("read","investment archive")
            }
        }
    }


}
