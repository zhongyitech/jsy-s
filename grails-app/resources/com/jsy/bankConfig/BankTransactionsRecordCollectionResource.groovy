package com.jsy.bankConfig

import com.jsy.bankServices.BankProxyService
import com.jsy.utility.DomainHelper

import javax.ws.rs.PUT
import javax.ws.rs.QueryParam


import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/bankTransactionsRecord')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BankTransactionsRecordCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    //记录已经处理过了
    static final int MANAGE_TYPE_Processed = 1
    //新的记录
    static final int MANAGE_TYPE_NEW = 0
    //弃用的记录
    static final int MANAGE_TYPE_DEL = 3
    BankTransactionsRecordResourceService bankTransactionsRecordResourceService
    BankProxyService bankProxyService

    @POST
    Response create(FundToBank dto) {
        ok {
            def ftb = bankTransactionsRecordResourceService.create(dto)
            ftb
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(BankTransactionsRecord, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
        }
    }

    @PUT
    @Path('/dealing')
    Response dealing(@QueryParam('id') Long id, @QueryParam('type') Long type) {
        ok {
            def ftb = BankTransactionsRecord.get(id)
            ftb.managed = true
            ftb.manageType = type
            ftb.save(failOnError: true)
            ftb
        }
    }

    /**
     * 设置某条记录为已正常处理
     * @param id
     * @return
     */
    @POST
    @Path('/processed')
    Response processed(@QueryParam('id') Long id) {
        ok {
            def ftb = BankTransactionsRecord.get(id)
            if (ftb == null || ftb.managed) {
                throw new Exception("OperationFailed, Not Found the ID  or The record is processed.")
            }
            ftb.setProcessedOK()
            ftb.save(failOnError: true)
            ftb
        }
    }

    @Path('/{id}')
    BankTransactionsRecordResource getResource(@PathParam('id') Long id) {
        new BankTransactionsRecordResource(bankTransactionsRecordResourceService: bankTransactionsRecordResourceService, id: id)
    }

    @GET
    @Path('/order')
    Response order() {
        ok {
            bankTransactionsRecordResourceService.ProcessTransactionData()
        }
    }

    @POST
    @Path('/banksAccounts')
    Response GetAccounts(Map arg) {
        page {
            // // ([account: "11007187041901", CcyType: "C", CcyCode: "RMB"]
            def data = BankAccount.list().collect {
                def res = [:]
                res.putAll(it.properties)
                res.putAll(
                        bankProxyService.QueryBalance([account: it.account, CcyType: "C", CcyCode: "RMB"])
                )
                return  res
            }
//            def result = []
//            arg.account.each {
//                result.add(bankProxyService.QueryBalance([account: it, CcyType: "C", CcyCode: "RMB"]))
//            }
//            result
            [data: data, total: data.size()]
        }
    }
}
