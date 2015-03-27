package com.jsy.archives

import com.jsy.fundObject.Finfo
import com.jsy.utility.JsonResult
import grails.converters.JSON
import org.json.JSONObject

import javax.ws.rs.DefaultValue
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

@Path('/api/contractRegister')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ContractRegisterCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    public static final String REST_INFORMATION = ""
    def contractRegisterResourceService

    @POST
    Response create(ContractRegister dto) {

        ok {
            def result=[:]
            def cr
            //截取合同编号
            int qs=Integer.parseInt(dto.qsbh.substring(5))
            int js=Integer.parseInt(dto.jsbh.substring(5))
            String bh=dto.qsbh.substring(0,5)
            //验证合同编号
            for(int i=qs;i<=js;i++){
                if (Contract.findByHtbh(bh+i)){
//                    rest_information = "已经存在的合同编号："+bh+i
//                    restStatus = REST_STATUS_FAI
//                    result.put("rest_information", rest_information)
//                    result.put("rest_status", restStatus)
                    result.put("rest_result", cr as JSON)
                    return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
                    break
                }
            }
            //循环新建合同
            for(int i=qs;i<=js;i++){
                    def c = new Contract(htbh: bh + i, fund: dto.fund, djsj: dto.djsj)
                    print(c.properties)
                    c.save(failOnError: true)
            }
            cr = dto.save(failOnError: true)
//            cr = contractRegisterResourceService.create(dto)

//            result.put("rest_status", restStatus)
            result.put("rest_result", cr as JSON)
            result
        }
        // {"fund":1, "qsbh":"CFYHN100","jsbh":CFYHN200,"sum":101}
//        String rest_information = REST_INFORMATION
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def cr
//        try {
//            //截取合同编号
//            int qs=Integer.parseInt(dto.qsbh.substring(5))
//            int js=Integer.parseInt(dto.jsbh.substring(5))
//            String bh=dto.qsbh.substring(0,5)
//            //验证合同编号
//            for(int i=qs;i<=js;i++){
//                if (Contract.findByHtbh(bh+i)){
//                    rest_information = "已经存在的合同编号："+bh+i
//                    restStatus = REST_STATUS_FAI
//                    result.put("rest_information", rest_information)
//                    result.put("rest_status", restStatus)
//                    result.put("rest_result", cr as JSON)
//                    return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//                    break
//                }
//            }
//            //循环新建合同
//            for(int i=qs;i<=js;i++){
//                    def c = new Contract(htbh: bh + i, fund: dto.fund, djsj: dto.djsj)
//                    print(c.properties)
//                    c.save(failOnError: true)
//            }
//            cr = dto.save(failOnError: true)
////            cr = contractRegisterResourceService.create(dto)
//
//            result.put("rest_status", restStatus)
//            result.put("rest_result", cr as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//            rest_information = "合同登记失败，请检查数据"
//            result.put("rest_information", rest_information)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", cr as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }

    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Finfo finfo) {
        page {
            def json = contractRegisterResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            return [data:json.get("page"),total:json.get("size")]
        }
//        print("contractRegisterResourceService.readAllForPage()")
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        int total
//        JSONObject json
//        def fp
//        try {
//            json = contractRegisterResourceService.readAllForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
//            total = json.get("size")
//            fp = json.get("page")
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI;
//            print(e)
//        }
//        result.put("rest_status", restStatus)
//        result.put("rest_result", fp as JSON)
//        result.put("rest_total", total)
//
//        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }


    @GET
    Response readAll() {
        ok contractRegisterResourceService.readAll()
    }

    @Path('/{id}')
    ContractRegisterResource getResource(@PathParam('id') Long id) {
        new ContractRegisterResource(contractRegisterResourceService: contractRegisterResourceService, id: id)
    }
}
