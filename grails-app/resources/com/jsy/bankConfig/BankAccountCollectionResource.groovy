package com.jsy.bankConfig

import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.utility.DomainHelper
import com.jsy.utility.JsonResult
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

//import static org.grails.jaxrs.response.Responses.*
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
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def bfpr
//        try {
//            bfpr = bankAccountResourceService.create(dto)
//
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI;
//            e.printStackTrace()
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", bfpr as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @PUT
    Response update(BankAccount dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def rc
            rc = bankAccountResourceService.update(dto)
            rc
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        dto.id = id
//        def rc
//        try {
//            rc = bankAccountResourceService.update(dto)
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", rc as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

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
//        org.json.JSONObject result = new org.json.JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        org.json.JSONObject json
//        def fp
//        try {
//            json = bankAccountResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
//            total = json.get("size")
//            fp = json.get("page")
//        } catch (Exception e) {
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fp as JSON)
//        result.put("rest_total", total)


//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @GET
    Response readAll() {
        ok bankAccountResourceService.readAll()
    }

    @GET
    @Path('/paylist')
    Response paybankaccounts(@QueryParam('fundName') String fundName) {
        ok {
            return FundCompanyInformation.findByFunds(Fund.findByFundName(fundName)).bankAccount
        }
//        Fund fund = Fund.findByFundName(fundName);
//        def resultObject;
//        def restStatus = REST_STATUS_SUC
//        try {
//            FundCompanyInformation company = FundCompanyInformation.findByFund(fund)
//            resultObject = company.bankAccount
//        }
//        catch (Exception e) {
//            restStatus = REST_STATUS_FAI
//            print(e)
//            return Response.status(Response.Status.NOT_FOUND).build()
//        }
//        def result = new HashMap()
//        result.put("rest_status", restStatus)
//        result.put("rest_result", resultObject)
//        JSON.use("deep")
//        return Response.ok(result as JSON).status(RESPONSE_STATUS_SUC).build()
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
//        try {
//            def result = BankAccount.findById(id)
//
//            print(result)
//            ok JsonResult.success(result)
//        }
//        catch (Exception e) {
//            ok JsonResult.error(e.message)
//        }
//        new BankAccountResource(bankAccountResourceService: bankAccountResourceService, id: id)
    }
}
