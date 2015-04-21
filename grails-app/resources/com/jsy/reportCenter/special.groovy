package com.jsy.reportCenter

/**
 * Created by lioa on 2015/4/2.
 */
import com.jsy.bankConfig.BankOrder
import com.jsy.bankConfig.BankOrderEntry
import com.jsy.flow.Dqztsq
import grails.converters.JSON
import groovy.sql.Sql

import javax.sql.DataSource
import javax.ws.rs.Consumes
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response
import static com.jsy.utility.MyResponse.*

/**
 * Created by lioa on 2015/3/26.
 */
@Path('/api/special')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class special {

    @GET
//    @Path('')
    Response getReport(@QueryParam("type") String type, @QueryParam("id") @DefaultValue("0") Long id) {
        ok {
            if (id == 0 || type == null || type.empty()) {
                throw new Exception("参数不正确")
            }
            switch (type) {
                case "dqzt":
                    def result = [:]
                    def sq = Dqztsq.get(id)
                    if(sq){
                        result.putAll(sq.properties)
                    }
                    return result
                    break
                default:
                    return null
                    break
            }
        }
    }
}