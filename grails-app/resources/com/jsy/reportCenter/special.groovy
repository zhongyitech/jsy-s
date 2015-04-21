package com.jsy.reportCenter

/**
 * Created by lioa on 2015/4/2.
 */
import com.jsy.bankConfig.BankOrder
import com.jsy.bankConfig.BankOrderEntry
import com.jsy.flow.Dqztsq
import com.jsy.flow.DqztsqResourceService
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
    static Map<String, Closure> _map = new HashMap<String, Closure>()
    static {
        _map.put("dqzt", {
            Long id ->
                Dqztsq.get(id)
        })
    }

    @GET
    @Path("/report")
    Response getReport(@QueryParam("reporttype") String stype, @QueryParam("id") Long id) {
        ok {
            _map.get(stype)?.call(id)
        }
    }
}