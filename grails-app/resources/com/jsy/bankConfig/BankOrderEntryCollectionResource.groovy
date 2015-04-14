package com.jsy.bankConfig

import com.jsy.utility.DomainHelper
import com.jsy.utility.MyResponse

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/bankOrderEntry')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BankOrderEntryCollectionResource {

    def bankOrderEntryResourceService
    def bankTransactionsRecordResourceService



    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        MyResponse.page {
            bankTransactionsRecordResourceService.ProcessTransactionData()
            DomainHelper.getPage(BankOrderEntry,arg)
//            [data:bankTransactionsRecordResourceService.ProcessTransactionData(),total:10]
        }
    }

    @Path('/{id}')
    BankOrderEntryResource getResource(@PathParam('id') Long id) {
        new BankOrderEntryResource(bankOrderEntryResourceService: bankOrderEntryResourceService, id: id)
    }
}
