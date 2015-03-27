package com.jsy.archives

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jsy.fundObject.Finfo
import com.jsy.project.TSProject
import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.json.JSONArray
import org.json.JSONObject
import static com.jsy.utility.MyResponse.*
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

//import static org.grails.jaxrs.response.Responses.*

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
    def commissionInfoResourceService

    @POST
    @Path('/addPayment')
    Response addPayment(CommissionInfo commissionInfo) {
        ok {
            def ci= commissionInfoResourceService.toPay(commissionInfo)
            ci
        }
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ci= commissionInfoResourceService.toPay(commissionInfo)
        result.put("rest_status", restStatus)
        result.put("rest_result", ci as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

//    @POST
//    Response create(CommissionInfo dto) {
//        created commissionInfoResourceService.create(dto)
//    }

    @GET
    Response readAll() {
        ok commissionInfoResourceService.readAll()
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
    Response updateType(@QueryParam('id') Long id,@QueryParam('sl') double sl,@QueryParam('sqsh') boolean sqsh){

        ok{
            CommissionInfo ci
            ci = commissionInfoResourceService.toPay(id)
            ci.sl = sl
            ci.sqsh = sqsh
            ci.save()
            ci
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        CommissionInfo ci
//        try {
////            ci = CommissionInfo.get(id)
//            ci = commissionInfoResourceService.toPay(id)
//            ci.sl = sl
//            ci.sqsh = sqsh
//            ci.save()
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

    @POST
    @Path('/getcommissionInfo')
    Response getcommissionInfo(String datastr) {
        page {
            def results
            def total
            org.json.JSONObject finfo = JSON.parse(datastr)

            def criterib = new DetachedCriteria(CommissionInfo).build {
                if(finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date1=sdf.parse(finfo.startsaledate1);
                    Date date2=sdf.parse(finfo.startsaledate2);


                    between("zfsj", date1, date2)
                }

                if(finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)){
                    or {
                        like("fundName", "%"+finfo.keyword+"%")
                        like("customer", "%"+finfo.keyword+"%")
                        like("ywjl", "%"+finfo.keyword+"%")         //业务经理
                        like("khh", "%"+finfo.keyword+"%")          //开户行
                        like("yhzh", "%"+finfo.keyword+"%")         //银行帐号
                    }
                }
                if(finfo.type){
                    eq("type",finfo.type)
                }else{                                              //不等于申请已处理的
                    or {
                        eq("type",0)
                        eq("type",1)
                    }
                }

                order("dateCreated", "desc")
            }

            def params = [:]
            params.max = 10
            params.offset = finfo.startposition ? finfo.startposition : 0

            results = criterib.list(params)
            total = criterib.size()
            results
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def results
//        def total
//        try {
//            org.json.JSONObject finfo = JSON.parse(datastr)
//
//            def criterib = new DetachedCriteria(CommissionInfo).build {
//                if(finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2){
//                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
//                    Date date1=sdf.parse(finfo.startsaledate1);
//                    Date date2=sdf.parse(finfo.startsaledate2);
//
//
//                    between("zfsj", date1, date2)
//                }
//
//                if(finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)){
//                    or {
//                        like("fundName", "%"+finfo.keyword+"%")
//                        like("customer", "%"+finfo.keyword+"%")
//                        like("ywjl", "%"+finfo.keyword+"%")         //业务经理
//                        like("khh", "%"+finfo.keyword+"%")          //开户行
//                        like("yhzh", "%"+finfo.keyword+"%")         //银行帐号
//                    }
//                }
//                if(finfo.type){
//                    eq("type",finfo.type)
//                }else{                                              //不等于申请已处理的
//                    or {
//                        eq("type",0)
//                        eq("type",1)
//                    }
//                }
//
//                order("dateCreated", "desc")
//            }
//
//            def params = [:]
//            params.max = 10
//            params.offset = finfo.startposition ? finfo.startposition : 0
//
//            results = criterib.list(params)
//            total = criterib.size()
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", results as JSON)
//        result.put("rest_total", total)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }


    @Path('/{id}')
    CommissionInfoResource getResource(@PathParam('id') Long id) {
        new CommissionInfoResource(commissionInfoResourceService: commissionInfoResourceService, id: id)
    }


    @GET
    @Path('/irData')
    Response initData() {
        commissionInfoResourceService.initData();
        ok "good"
    }
}
