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

@Path('/api/payment')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class PaymentCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def paymentResourceService

    @POST
    Response create(Payment dto,@QueryParam('id') Long id) {
        ok {
            dto.id = id
            dto = paymentResourceService.create(dto)
            dto
        }
//        dto.id = id
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def payment
//        try {
//            dto = paymentResourceService.create(dto)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", dto as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response readAll() {
        ok {
            def ret = [:]
            def pc = paymentResourceService.readAll()
            ret.all = pc
            ret.total = pc.count()
            ret

        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        def payment
//        try {
//            payment = paymentResourceService.readAll()
//            total = payment.size()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", payment as JSON)
//        result.put("rest_total", total)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path('/getPayments')
    Response getPayments(String datastr) {
        ok {
            def ret = [:]
            int total
            def results
            org.json.JSONObject finfo = JSON.parse(datastr)

            def criterib = new DetachedCriteria(Payment).build {
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
                eq("dflx",finfo.type)

                if(finfo.has('status') && finfo.status){
                    eq("status", finfo.status)
                }else{
                    between("status", 0, 1)
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
//        int total
//        def results
//        try {
//            org.json.JSONObject finfo = JSON.parse(datastr)
//
//            def criterib = new DetachedCriteria(Payment).build {
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
//                eq("dflx",finfo.type)
//
//                if(finfo.has('status') && finfo.status){
//                    eq("status", finfo.status)
//                }else{
//                    between("status", 0, 1)
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
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", results as JSON)
//        result.put("rest_total", total)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @POST
    @Path('/getCommissions')
    Response getCommissions(String datastr) {
        ok {
            def results
            int total
            def ret = [:]
            org.json.JSONObject finfo = JSON.parse(datastr)

            def criterib = new DetachedCriteria(Payment).build {
                if(finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date1=sdf.parse(finfo.startsaledate1);
                    Date date2=sdf.parse(finfo.startsaledate2);

                    between("zfsj", date1, date2)
                }


                if(finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)){
                    or {
                        like("fundName", "%"+finfo.keyword+"%")
                        like("contractNum", "%"+finfo.keyword+"%")
                        like("customerName", "%"+finfo.keyword+"%")         //业务经理
                    }
                }
                eq("dflx",finfo.type)

                if(finfo.has('status') && finfo.status){
                    eq("status", finfo.status)
                }else{
                    between("status", 0, 1)
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
//        def results
//        int total
//        try {
//            org.json.JSONObject finfo = JSON.parse(datastr)
//
//            def criterib = new DetachedCriteria(Payment).build {
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
//                        like("contractNum", "%"+finfo.keyword+"%")
//                        like("customerName", "%"+finfo.keyword+"%")         //业务经理
//                    }
//                }
//                eq("dflx",finfo.type)
//
//                if(finfo.has('status') && finfo.status){
//                    eq("status", finfo.status)
//                }else{
//                    between("status", 0, 1)
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
//
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", results as JSON)
//        result.put("rest_total", total)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }
//
//    @POST
//    @Path('/getCommissions')
//    Response getCommissions(Finfo finfo,@QueryParam('type') String type) {
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def payment
//        int total
//        try {
//            payment = paymentResourceService.getCommissions(finfo,type)
//            total=paymentResourceService.getCommissionsTotal(finfo,type)
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", payment as JSON)
//        result.put("rest_total", total)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//    }

    //应该再加个到银行的接口
    @GET
    @Path('/toPay')
    Response toPay(@QueryParam('ids') String ids) {
        ok {
            JSONArray jsonArray=new JSONArray()
            ids.split(",").each {
                JSONObject jbo=new JSONObject()
                boolean b=true
                Long payId=Long.valueOf(it)
                try{
                    paymentResourceService.updatePayment(Payment.get(payId),1)
                }catch (Exception e){
                    b=false
                }
                jbo.put("id",payId)
                jbo.put("result",b)
                jsonArray.put(jbo)
            }
            jsonArray
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        JSONArray jsonArray=new JSONArray()
//        ids.split(",").each {
//            JSONObject jbo=new JSONObject()
//            boolean b=true
//            Long payId=Long.valueOf(it)
//            try{
//                paymentResourceService.updatePayment(Payment.get(payId),1)
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

    @Path('/{id}')
    PaymentResource getResource(@PathParam('id') Long id) {
        new PaymentResource(paymentResourceService: paymentResourceService, id:id)
    }
}
