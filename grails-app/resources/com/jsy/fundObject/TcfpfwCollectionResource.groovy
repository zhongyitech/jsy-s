package com.jsy.fundObject

import com.jsy.auth.Role
import com.jsy.auth.User
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

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

@Path('/api/tcfpfw')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class TcfpfwCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    def tcfpfwResourceService

    @POST
    Response create(Tcfpfw dto) {
        created tcfpfwResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok tcfpfwResourceService.readAll()
    }

    @Path('/{id}')
    TcfpfwResource getResource(@PathParam('id') Long id) {
        new TcfpfwResource(tcfpfwResourceService: tcfpfwResourceService, id: id)
    }

    @GET
    @Path('/getcommision')
    Response getcommision(@QueryParam('managerid') Long managerid,@QueryParam('fundid') Long fundid){
        ok {
            def commision
            Fund fund
            def tcfpfw
            fund = Fund.findById(fundid)
                def t1 = Tcfpfw.findAllByManageerId(managerid)
                tcfpfw = fund.tcfpfw

                tcfpfw.each {t->
                    t1.each {r->
                        if (t.id == r.id){
                            commision = t
                        }
                    }
                }

                print(commision)
                if (null != commision){
                    if (true == commision.allSell){
                        commision.manageCommision = 0
                        commision.businessCommision = commision.investment - fund.yieldRange.yield
                    }
                }
            commision
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def commision
//        Fund fund
//        def tcfpfw
////        try {
////            def r = User.findById(managerid)
//            fund = Fund.findById(fundid)
//            if (/*null != r && */null != fund){
//                def t1 = Tcfpfw.findAllByManageerId(managerid)
//                print(t1)
//                tcfpfw = fund.tcfpfw
//                print(tcfpfw)
////                commision = tcfpfw.findAll(t1)
////                commision = Tcfpfw.findAllByManageerIdInList(tcfpfw,managerid.toInteger())
////                commision = Tcfpfw.findAllByManageerIdInList(managerid.toInteger(),Fund.findById(fundid).tcfpfw)
////                commision = Tcfpfw.findAllByManageerIdInListAndManageerId(tcfpfw,managerid.toInteger())
//
//                tcfpfw.each {t->
//                    t1.each {r->
//                        if (t.id == r.id){
//                            commision = t
//                        }
//                    }
//                }
//
//                print(commision)
//                if (null != commision){
//                    if (true == commision.allSell){
//                        commision.manageCommision = 0
//                        commision.businessCommision = commision.investment - fund.yieldRange.yield
//                    }
//                }
//            }else {
//                print("managerid or fundid is not exist!")
//            }
////        }catch (Exception e){
////            restStatus = REST_STATUS_FAI
////            print(e)
////        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", commision as JSON)
////        result.put("rest_fundyield", fund.yieldRange.yield as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/gettcfpfwinfo')
    Response gettcfpfwinfo(@QueryParam('managername') String mname,@QueryParam('fundname') String fname){
        ok {
            def ret =[:]
            def commision = Tcfpfw.findByManageerId(Role.findByName(mname).id)
            def fund = Fund.findByFundName(fname)
            ret.commision = commision
            ret.yield = fund.yieldRange.yield
            ret
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def commision
//        Fund fund
//        try {
//            commision = Tcfpfw.findByManageerId(Role.findByName(mname).id)
//            fund = Fund.findByFundName(fname)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", commision as JSON)
//        result.put("rest_fundyield", fund.yieldRange.yield as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/gettcfpfwbyid')
    Response gettcfpfwbyid(@QueryParam('id') Long id){
        ok {
            def tc = Tcfpfw.findById(id)
            tc
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def commision
//        Fund fund
//        def tc
//        try {
//            tc = Tcfpfw.findById(id)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", tc as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

}
