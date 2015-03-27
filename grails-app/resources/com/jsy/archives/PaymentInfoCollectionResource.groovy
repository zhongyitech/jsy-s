package com.jsy.archives

import com.jsy.fundObject.Finfo
import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.json.JSONArray
import org.json.JSONObject

import javax.ws.rs.DefaultValue
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

//import static org.grails.jaxrs.response.Responses.*
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

    def paymentInfoResourceService

//    @POST
//    Response create(PaymentInfo dto) {
//        created paymentInfoResourceService.create(dto)
//    }

    @GET
    @Path('/toPay')
    Response toPay(@QueryParam('payIds') String payIds){
        ok {
            JSONArray jsonArray=new JSONArray()
            payIds.split(",").each {
                JSONObject jbo=new JSONObject()
                boolean b=true
                Long payId=Long.valueOf(it)
                    def dto=paymentInfoResourceService.toPay(payId)
                jbo.put("id",payId)
                jbo.put("result",b)
                jsonArray.put(jbo)
            }
            jsonArray
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        JSONArray jsonArray=new JSONArray()
//        payIds.split(",").each {
//            JSONObject jbo=new JSONObject()
//            boolean b=true
//            Long payId=Long.valueOf(it)
//            try{
//                def dto=paymentInfoResourceService.toPay(payId)
//            }catch (Exception e){
//                b=false
//            }
//            jbo.put("id",payId)
//            jbo.put("result",b)
//            jsonArray.put(jbo)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", jsonArray.toString())
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path("/readAllForPage")
    Response readAllForPage(String datastr) {
        ok {
            org.json.JSONObject finfo = JSON.parse(datastr)
            JSONObject result = new JSONObject();
            String restStatus = REST_STATUS_SUC;
            def total
            def results
            def ret = [:]

            def criterib = new DetachedCriteria(PaymentInfo).build {
                if(finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date1=sdf.parse(finfo.startsaledate1);
                    Date date2=sdf.parse(finfo.startsaledate2);

                    between("zfsj", date1, date2)
                }


                if(finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)){
                    or {
                        like("fundName", "%"+finfo.keyword+"%")
                        like("customerName", "%"+finfo.keyword+"%")         //业务经理
                    }
                }
                eq("isAllow",false)

                if(finfo.has('type') && finfo.type){
                    eq("type", finfo.type)
                }else{
                    between("type", 0, 1)
                }

                order("dateCreated", "desc")
            }

            def params = [:]
            params.max = 10
            params.offset = finfo.startposition ? finfo.startposition : 0

            results = criterib.list(params)
            total = criterib.size()
            ret.result = results
            ret.total = total
            ret
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//
//        //查询生成对付记录
//        try{
//            paymentInfoResourceService.addPaymentInfo()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//        }
//
//        def total
//        def results
//        try {
//
//            org.json.JSONObject finfo = JSON.parse(datastr)
//
//            def criterib = new DetachedCriteria(PaymentInfo).build {
//                if(finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2){
//                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
//                    Date date1=sdf.parse(finfo.startsaledate1);
//                    Date date2=sdf.parse(finfo.startsaledate2);
//
//                    between("zfsj", date1, date2)
//                }
//
//
//                if(finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)){
//                    or {
//                        like("fundName", "%"+finfo.keyword+"%")
//                        like("customerName", "%"+finfo.keyword+"%")         //业务经理
//                    }
//                }
//                eq("isAllow",false)
//
//                if(finfo.has('type') && finfo.type){
//                    eq("type", finfo.type)
//                }else{
//                    between("type", 0, 1)
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
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", results as JSON)
//        result.put("rest_total", total)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @Path('/{id}')
    PaymentInfoResource getResource(@PathParam('id') Long id) {
        new PaymentInfoResource(paymentInfoResourceService: paymentInfoResourceService, id: id)
    }
}
