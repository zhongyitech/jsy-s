package com.jsy.bankConfig

import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
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

@Path('/api/bankAccount')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class BankAccountCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def bankAccountResourceService

    @POST
    Response create(BankAccount dto) {
        ok {
            def bfpr = bankAccountResourceService.create(dto)
            bfpr
        }
    }

    @PUT
    Response update(BankAccount dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def rc
            rc = bankAccountResourceService.update(dto)
            rc
        }
    }


    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(BankAccount, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count():0]
        }
    }


    @GET
    @Path('/paylist')
    Response paybankaccounts(@QueryParam('fundName') String fundName) {
        ok {
            return FundCompanyInformation.findByFunds(Fund.findByFundName(fundName)).bankAccount
        }

    }

    /**
     * 获取单个银行账户信息
     * @param id
     * @return
     */
    @GET
    @Path('/{id}')
    Response getResource(@PathParam('id') Long id) {
        ok {
            def result = BankAccount.findById(id)
            result
        }
    }



}
