package com.jsy.utility

import grails.gorm.DetachedCriteria
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * 数据读取帮助类;数据分页和通用条件过滤
 * Created by lioa on 2015/3/19.
 */
@Transactional(rollbackFor = Throwable.class)
class DomainHelper {
    public static DetachedCriteria getDetachedCriteria(Class domain, def query) {
        VaildQueryObj(query)
        DetachedCriteria dc = new DetachedCriteria(domain)
        if (query == null) {
            return dc;
        }
        Map orderArg = query.order

        return dc.where {
            if (query.type == 'and') {
                Map condition = query.condition
                if (condition && condition != "") {
                    and {
                        condition.each {
                            entity -> eq(entity.key, entity.value)
                        }
                    }
                }
                if (orderArg) {
                    orderArg.each {
                        entity ->
                            order(entity.key, entity.value)
                    }
                }
            } else {
                //or
                if (query.value && query.value != "") {
                    or {
                        query.fields.each {
                            String item ->
                                like(item, "%${query.value}%")
                        }
                    }
                }
                if (orderArg) {
                    orderArg.each {
                        entity ->
                            order(entity.key, entity.value)
                    }
                }
            }
        }
    }

    /**
     * 返回指定对象的分页数据
     * @param domainClass 对象名称(表名)
     * @param query 查询条件
     * @return page格式
     */
    public static Map getPage(Class domainClass, def query) {
        def dc = DomainHelper.getDetachedCriteria(domainClass, query)
        return [data: dc.list([max: query.pagesize, offset: query.startposition]), total:  dc.count()]
    }

    /**
     * 检测查询参数的合法性
     * @param query
     */
    private static void VaildQueryObj(def query) {
        if (query == null || (query as Map) == null) {
            throw new Exception("查询格式不正确")
        }
    }

    public static def toMap(def target) {

        def json = new JSONObject(target)
        return json
    }
}
