package com.jsy.PublicAPI

import com.jsy.bankConfig.BankOrderEntry
import com.jsy.utility.BankFormat

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
@Path('/api/test/outApi')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class SudaMoneyTest {
    private static List<BankOrderEntry> _list = new ArrayList<BankOrderEntry>()

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
//         BankOrderEntry.findAllByManageType(NoAccept)
            if(_list.size()==0){
                _list=BankOrderEntry.list()
            }
            return _list.findAll {
                it.manageType == NoAccept
            }
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
            def order = _list.find { it.id == id }
            if (order == null) {
                throw new Exception("ID 不正确")
            }
            order.manageType = AcceptOK
//            order.save(failOnError: true)
            return true
        }
    }
}