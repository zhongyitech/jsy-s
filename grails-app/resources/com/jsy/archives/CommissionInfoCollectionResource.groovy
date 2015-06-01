package com.jsy.archives

import com.jsy.utility.DomainHelper
import com.jsy.utility.MyException
import grails.converters.JSON
import org.json.JSONObject
import static com.jsy.utility.MyResponse.*
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/commissionInfo')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class CommissionInfoCollectionResource {

    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    CommissionInfoResourceService commissionInfoResourceService

    /**
     * 生成提成申请单
     * @param commissionInfo
     * @return
     */
    @POST
    @Path('/addPayment')
    Response addPayment(@QueryParam('comId') int comId) {
        ok {
            def commision = CommissionInfo.get(comId)
            if (commision == null) throw new MyException("提成数据ID不正确")
            def ci = commissionInfoResourceService.toPay(comId, commision.sfyfse, commision.fpje, commision.sj, commision.fkje, commision.sqsh, commision.sl)
            ci
        }
    }

//    @POST
//    Response create(CommissionInfo dto) {
//        created commissionInfoResourceService.create(dto)
//    }

    @GET
    Response readAll() {
        ok { commissionInfoResourceService.readAll() }
    }

    @GET
    @Path('/getByarchivesId')
    Response getByarchivesId(@QueryParam('archivesId') Long archivesId) {
        ok {
            def ci = CommissionInfo.findAllByArchivesId(archivesId)
            ci
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def ci
//        try {
//            ci = CommissionInfo.findAllByArchivesId(archivesId)
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", ci as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @PUT
    @Path('/updateType')
    Response updateType(@QueryParam('id') Long id, @QueryParam('sl') double sl, @QueryParam('sqsh') boolean sqsh) {

        ok {
            CommissionInfo ci
            ci = commissionInfoResourceService.toPay(id)
            ci.sl = sl
            ci.sqsh = sqsh
            ci.save()
            ci
        }
    }

    /**
     * 获取提成的分页数据
     * @param arg 分页和字段过滤数据
     * @return
     */
    @POST
    @Path('/getcommissionInfo')
    Response getcommissionInfo(Map arg) {
        page {
            try {
                commissionInfoResourceService.addCommissionInfo()
            }catch (Exception ex){
                //不对异常进行处理，有一个后台任务在执行些操作
            }
            def page = DomainHelper.getPage(CommissionInfo, arg)
            return page
        }
    }

    @Path('/{id}')
    CommissionInfoResource getResource(@PathParam('id') Long id) {
        new CommissionInfoResource(commissionInfoResourceService: commissionInfoResourceService, id: id)
    }

    @GET
    @Path('/irData')
    Response initData() {
        commissionInfoResourceService.initData();
        ok {
            "ok"
        }
    }
}
