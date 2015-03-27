package com.jsy.PublicAPI

import com.jsy.bankConfig.BankOrder
import com.jsy.bankConfig.BankOrderEntry
import grails.converters.JSON

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
@Path('/api/PublicAPI')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SudaMoney {

    /**
     * 未处理
     */
    static final int NoAccept = 0
    /**
     * 已处理
     */
    static final int AcceptOK = 1

    /**
     * 与速达账务软件的对接API
     */
    @GET
    @Path('/bankOrder')
    Response getBankOrder() {
        ok {
//            JSON.use("deep")
            def order = BankOrder.findAllByManageType(NoAccept)
            def o = [:]
            o.number = order.evidenceCode
            o.company = order.fund
            o.date = order.evidenceDate
            o.zihao = order.evidenceKey
            o.zhenghao = order.evidenceValue
            o.writedate = order.createDate
            o.status = order.manageType == 0 ? false : true
            def oe = []
            BankOrderEntry.findAllByEvidenceCode(order.evidenceCode)
                    .each {
                oe.push(
                        [summary: it.summary, subjectName: it.subjectName, lamount: it.lendAmount, bamount: it.borrowAmount, transactionNo: it.transaction]
                )
            }
            o.entry=oe

            return o
        }
    }
    /**
     * 标识某条数据已经被处理了
     * @param id
     * @return
     */

    @POST
    @Path('/Accept')
    Response accept(@QueryParam('id') Long id) {
        ok {
            def order = BankOrder.get(id)
            order.manageDate = new Date()
            order.manageType = AcceptOK
            order.save(failOnError: true)
            return true
        }
    }
}