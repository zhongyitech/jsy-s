package com.jsy.reportCenter

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.archives.PaymentInfo

/**
 * Created by lioa on 2015/4/2.
 */
import com.jsy.bankConfig.BankOrder
import com.jsy.bankConfig.BankOrderEntry
import com.jsy.fundObject.Fund
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException
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
 * df
 * Created by lioa on 2015/3/26.
 */
@Path('/api/report')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class summary {

    DataSource dataSource

    /**
     * ���ļ�����Ƽ��Ա�ͼ
     * @return
     */
    @GET
    @Path('/fundTrend')
    Response summaryFund() {
        ok {
            try{
                def sql = new Sql(dataSource)
                def list = sql.rows("SELECT * FROM fund_month_summary")
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
            }catch(Exception e){
                if(e.getLocalizedMessage().indexOf("Table 'test1.fund_month_summary' doesn't exist")==-1){
                    throw e
                }
            }
        }
    }
    /**
     *
     * @return
     */
    @GET
    @Path('/fundSalesForMonth')
    Response getSalesForMonth(@QueryParam('id') Long id) {
        ok {
            print(id);
            def sql = new Sql(dataSource)
            def list = sql.rows("SELECT * FROM fund_month_summary")
            list
        }
    }
    /**
     * ��ȡָ����Ƶı���
     * @param name
     * @param id
     * @return
     */
    @GET
    @Path('/fundReportName')
    Response fundReportName(@QueryParam('name') String name, @QueryParam('id') Long id) {
        ok {
            print(id);
            def sql = new Sql(dataSource)
            switch (name) {
                case 'fundSummary':
                    def result = [:];
                    def fund = Fund.get(id);
                    def ivs = InvestmentArchives.findAllByFund(fund);
                    //todo:���ͳ��ʱ��εĹ���
                    def amount = ivs.sum {
                        InvestmentArchives iv ->
                            iv.tzje
                    };
                    //���۶�
                    result.put('amount', amount);
                    //�Ҹ�
                    def pay = []
                    def tcs = []
                    ivs.each {
                        InvestmentArchives iv ->
                            PaymentInfo.findAllByArchivesIdAndIsAllow(iv.id, true).each {
                                pay.push([amount: it.yflx + it.yfbj, date: it.fxsj])
                            };
                            iv.gltcs.each {
                                def date = it.real_glffsj3 ? it.real_glffsj3 : it.real_glffsj2 ? it.real_glffsj2 : it.sjffsj
                                if (date)
                                    tcs.push([amount: it.tcje, stype: 'gl', date: date])
                            }
                            iv.ywtcs.each {
                                if (it.sjffsj)
                                    tcs.push([amount: it.tcje, stype: 'yw', date: it.sjffsj])
                            }
                    }
                    result.put('pay', pay)
                    result.put('tc', tcs)

                    //TODO:����������Ŀ��Ϣ
                    return result

                    break;
                case 'fundSales':
                    break;
            }
            null
        }
    }

    @GET
    @Path('/fundTzje')
    Response getAmountData(@QueryParam('id') Long id) {
        ok {
            def sql = new Sql(dataSource)
            def list = sql.rows("SELECT sum(ia.tzje) as tzje,f.raise_funds rtzje ,f.fund_name as fundName,f.id\n" +
                    "FROM investment_archives as ia\n" +
                    "join fund as f on f.id=ia.fund_id\n" +
                    "WHERE f.id=" + id);
            list
        }
    }
}