package com.jsy.reportCenter

/**
 * Created by lioa on 2015/4/2.
 */
import com.jsy.bankConfig.BankOrder
import com.jsy.bankConfig.BankOrderEntry
import grails.converters.JSON
import groovy.sql.Sql

import javax.sql.DataSource
import javax.ws.rs.Consumes
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
@Path('/api/report')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class summary {

    DataSource dataSource

    /**
     * 基金募集趋势及对比图
     * @return
     */
    @GET
    @Path('/fundTrend')
    Response summaryFund() {
        ok {

            def sql = new Sql(dataSource)
            def list = sql.rows("SELECT * FROM fund_month_summry")
            print(list)
            def columns = []
            Map columnMap = [:]
            list.each {
                print(it)
                columns.push(it.tz_date)
                columnMap.put(it.fund_name + "-" + it.tz_date, it.tzje_amount)
            }
            def funds = []
            list.each {
                if (!funds.contains(it.fund_name)) {
                    funds.push(it.fund_name)
                }
            }

            def items = funds.collect {
                entity ->

                    def item = [name: entity]
                    def data = []
                    columns.each {
                        l ->
                            data.push(
                                    columnMap.containsKey(entity + "-" + l) ? columnMap[entity + "-" + l] : 0
                            )
                    }
                    item.data = data
                    return item
            }
            return [columns: columns, items: items]
        }
    }
}