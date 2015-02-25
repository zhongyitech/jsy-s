package com.jsy.customerObject

import com.jsy.fundObject.Finfo
import com.jsy.system.InvestmentProfileService
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/customer')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class CustomerCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def customerResourceService
    @POST
    Response create(Customer dto) {
    JSONObject result = new JSONObject();
    String restStatus = REST_STATUS_SUC;
    def custm
    try{
//        StringBuffer funno = new StringBuffer("C")
//        Date d = new Date()
//        print(d)
////            dto.createDate = d
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
//        String da=sdf.format(d)
//        funno.append(da.toString())
//        int endnumber = Math.random()*10000 + Math.random()*1000 + Math.random()*100 + Math.random()*10
//        StringBuffer funnocut = new StringBuffer(funno)
//        print(funno)
//        funnocut.append(endnumber)
//        print(funno)
//        dto.cu = funnocut
//        rc=registerContractResourceService.create(dto)
//        print(rc.id)
//        print(funno)
//        funno.append(rc.id)
//        print(funno)
//        rc.indexNum = funno
//        print(funno)
//        rc.save(failOnError: true)
        custm = customerResourceService.create(dto)

    }catch (Exception e){
        restStatus = REST_STATUS_FAI
        print(e)
    }
    result.put("rest_status", restStatus)
    result.put("rest_result", custm as JSON)
    return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def custm
        try{
            custm = customerResourceService.readAll()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", custm as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @GET
    @Path('/getcustomer')
    Response getcustomer(@QueryParam('cid') Long cid){
        print('getcustomer'+cid)
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def custm
        try{
            custm = Customer.get(cid)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", custm as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()


    }

    @PUT
    @Path('/update')
    Response update(Customer dto,@QueryParam('id') Long id){
        print("CustomerCollectionResources.update()")
        dto.id = id
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def custm
        try{
            custm = customerResourceService.update(dto)
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        }catch (Exception e){
            restStatus = REST_STATUS_FAI
            print(e)
            result.put("rest_status", restStatus)
            result.put("rest_result", custm as JSON)
            return Response.ok(result.toString()).status(500).build()
        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", custm as JSON)
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        return "suc"
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
//        pagesize = 10
//        startposition = 0
//        queryparam = ""
//        print(startposition)
//        print(pagesize)
        if(null == finfo.keyword){
            finfo.keyword = ""
        }
        print("customerResourceService.readallforPage")
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def customer
        def total
        try {
            customer = Customer.findAllByNameLike("%"+finfo.keyword+"%")
            total = customer.size()
            customer = customerResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
        }catch (Exception e){
            restStatus = REST_STATUS_FAI;
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", customer as JSON)
        result.put("rest_total", total)

        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }

    @Path('/{id}')
    CustomerResource getResource(@PathParam('id') Long id) {
        new CustomerResource(customerResourceService: customerResourceService, id:id)
    }


    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username){
        def users=Customer.findAllByNameLike("%"+username+"%")
        JSONArray jsonArray=new JSONArray()
        users.each {
            JSONObject jso=new JSONObject()
            jso.put("value",it.name)
            jso.put("data",it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject=new JSONObject()
        jsonObject.put("query","Unit")
        jsonObject.put("suggestions",jsonArray)

        ok jsonObject.toString()
    }
}
