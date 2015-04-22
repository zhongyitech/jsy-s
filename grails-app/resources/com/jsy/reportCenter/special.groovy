package com.jsy.reportCenter

import com.jsy.archives.Contract
import com.jsy.archives.InvestmentArchives
import com.jsy.auth.Menus
import com.jsy.auth.User
import com.jsy.bankConfig.BankAccount

/**
 * Created by lioa on 2015/4/2.
 */
import com.jsy.bankConfig.BankOrder
import com.jsy.bankConfig.BankOrderEntry
import com.jsy.customerObject.Customer
import com.jsy.flow.Dqztsq
import com.jsy.flow.DqztsqResourceService
import com.jsy.flow.Thclsq
import com.jsy.flow.Wdqztsq
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.system.Department
import com.jsy.system.OperationRecord
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
        //到期转投申请
        _map.put("dqzt", {
            Long id ->
                def sq = Dqztsq.get(id)
                if (sq) {
                    def result = [id: sq.id]
                    result.putAll(sq.properties)
                    def company = InvestmentArchives.get(sq.oldArchivesId).fund.funcCompany
                    result.putAt("oldArchives", InvestmentArchives.get(sq.oldArchivesId))
                    result.putAt("newArchives", InvestmentArchives.get(sq.newArchivesId))
                    result.put("company", company.properties)
                    return result
                }
                return null
        })
        //退伙申请
        _map.put("thdgp", {
            Long id ->
                def sq = Thclsq.get(id)
                if (sq) {
                    def result = [id: sq.id]
                    result.putAll(sq.properties)
                    def company = InvestmentArchives.get(sq.oldArchivesId).fund.funcCompany
                    result.putAt("oldArchives", InvestmentArchives.get(sq.oldArchivesId))
                    result.put("company", company.properties)
                    return result
                }
                return null
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