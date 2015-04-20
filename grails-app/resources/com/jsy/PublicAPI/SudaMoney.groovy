package com.jsy.PublicAPI

import com.jsy.bankConfig.BankOrderEntry

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
@Path('/api/outApi')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SudaMoney {

    /**
     * 接口的token码
     */
    public static String ApiToken_Suda="safliCijaifdslafNdsaflknadISldfknaSONm0NBsdf"

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
//            def order = BankOrder.findAllByManageType(NoAccept)
//            def o = [:]
//            o.number = order.evidenceCode
//            o.company = order.fund
//            o.date = order.evidenceDate
//            o.zihao = order.evidenceKey
//            o.zhenghao = order.evidenceValue
//            o.writedate = order.createDate
//            o.status = order.manageType == 0 ? false : true
//            def oe = []
//            BankOrderEntry.findAllByEvidenceCode(order.evidenceCode)
//                    .each {
//                oe.push(
//                        [summary: it.summary, subjectName: it.subjectName, lamount: it.lendAmount, bamount: it.borrowAmount, transactionNo: it.transaction]
//                )
//            }
//            o.entry=oe
//            return o
            BankOrderEntry.findAllByManageType(NoAccept)
        }
    }

    @GET
    @Path('/test')
    Response test(){
        ok{
            return [a:100,b:100]
        }
    }

    /**
     * 标识某条数据已经被处理了
     * @param id
     * @return
     */

    @POST
    @Path('/bankOrderAccept')
    Response accept(@QueryParam('id') Long id) {
        ok {
            def order=BankOrderEntry.get(id)
            if(order==null){
                throw  new Exception("ID 不正确")
            }
            order.manageType=AcceptOK
            order.save(failOnError: true)
            return true
        }
    }
}