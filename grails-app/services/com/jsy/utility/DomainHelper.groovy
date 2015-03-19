package com.jsy.utility

import grails.gorm.DetachedCriteria

/**
 * Created by lioa on 2015/3/19.
 */
class DomainHelper {
    public static DetachedCriteria getDetachedCriteria(Class domain, def query) {
        DetachedCriteria dc = new DetachedCriteria(domain)
        Map condition = query.condition

        Map orderArg = query.order
        return dc.where {
            if (query.type == 'and') {
                and {
                    condition.each {
                        entity -> eq(entity.key, entity.value)
                    }
                }
                if (orderArg) {
                    orderArg.each {
                        entity ->
                            order(entity.key, entity.value)
                    }
                }
            } else {
                def value = condition.value
                if(value&&value!=""){
                    or {
                        condition.fields.each {
                            String item ->
                                like(item, "%${condition.value}%")
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
}
