package com.jsy.archives

import com.jsy.system.OperationRecord
import com.jsy.utility.DomainHelper
import com.jsy.utility.MyResponse
import grails.gorm.DetachedCriteria
import org.grails.jaxrs.response.Responses
import org.json.JSONArray
import org.json.JSONObject

import javax.ws.rs.QueryParam
import static com.jsy.utility.MyResponse.*


import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/paymentInfo')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class PaymentInfoCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    PaymentInfoResourceService paymentInfoResourceService

    @GET
    @Path('/toPay')
    Response toPay(@QueryParam('payIds') String payIds) {
        ok {
            JSONArray jsonArray = new JSONArray()
            payIds.split(",").each {
                JSONObject jbo = new JSONObject()
                boolean b = true
                Long payId = Long.valueOf(it)
                def dto = paymentInfoResourceService.toPay(payId)
                jbo.put("id", payId)
                jbo.put("result", b)
                jsonArray.put(jbo)
            }
            jsonArray
        }
    }

    @POST
    @Path("/readAllForPage")
    Response readAllForPage(Map arg) {


        MyResponse.page {

            //生成付息数据
            paymentInfoResourceService.addPaymentInfo()

            print(arg)
            def page = DomainHelper.getPage(PaymentInfo, arg)
            return page
        }
    }

    /**
     *获取统计信息
     * @param arg
     * @return
     */
    @POST
    @Path('/summary')
    Response getSummary(Map arg) {

        MyResponse.page {
            boolean payStatus = arg.payStatus;
            def data = PaymentInfo.createCriteria().list {
                if (payStatus != null) {
                    eq("isAllow", payStatus)
                }
                projections {
                    groupProperty("fundName")
                    sum("yfbj")
                    sum("yflx", "yflx")
                    rowCount("id")
                }
                maxResults(arg.pagesize == null ? 10 : arg.pagesize)
            }.collect {
                [fundName: it[0], amount: it[1] + it[2], bj: it[1], lx: it[2], count: it[3]]
            }
            return [data: data, total: data.size()]
        }
    }

    @Path('/{id}')
    PaymentInfoResource getResource(@PathParam('id') Long id) {
        new PaymentInfoResource(paymentInfoResourceService: paymentInfoResourceService, id: id)
    }
}
