package com.jsy.utility

import grails.gorm.DetachedCriteria

/**
 * Created by lioa on 2015/3/19.
 */
class DomainHelper {
    public static DetachedCriteria getDetachedCriteria(Class domain, def query) {
        DetachedCriteria dc = new DetachedCriteria(domain)
        if(query==null){
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
}
