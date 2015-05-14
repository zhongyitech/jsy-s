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
 * 速达软件接口
 * Created by lioa on 2015/3/26.
 */
@Path('/api/outApi')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SudaMoney {

    /**
     * 接口的token码
     */
    public static String ApiToken_Suda = "A21B6261F60646858F44FD79EDC84B3B"

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
            println("Get Order")
            BankOrderEntry.findAllByManageType(NoAccept)
        }
    }

    @GET
    @Path('/test')
    Response test() {
        ok {
            return [a: 100, b: 100]
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
            def order = BankOrderEntry.get(id)
            if (order == null) {
                throw new Exception("ID 不正确")
            }
            println("bankOrderAccept")
            order.manageType = AcceptOK
            order.processedDate = new Date()
            order.save(failOnError: true)
            return true
        }
    }

    @POST
    @Path('/bankOrderFailed')
    Response noAccept(@QueryParam('id') Long id, @QueryParam("error") String error) {
        ok {
            println("bankOrderFailed")
            def order = BankOrderEntry.get(id)
            if (order == null) {
                throw new Exception("ID 不正确")
            }
            order.processedDate = new Date()
            order.bz = error
            order.save(failOnError: true)
            return true
        }
    }
}