package com.jsy.fundObject

import com.jsy.archives.Contract
import com.jsy.utility.CreateNumberService
import com.jsy.utility.DomainHelper
import com.jsy.utility.JsonResult
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DELETE
import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
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

@Path('/api/registerContract')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class RegisterContractCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    public static final String REST_INFORMATION = "";
    public static int nameLength = 5
    public static int numberLength = 4
    def registerContractResourceService

    @POST
    Response create(RegisterContract dto) {
        ok {
            def rc
            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
            rc = registerContractResourceService.create(dto)
            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
            rc.save(failOnError: true)
            rc
        }
    }

    @DELETE
    Response delete(@PathParam('id') Long id) {
        ok {

            return registerContractResourceService.delete(id)
        }
    }

    @PUT
    Response update(RegisterContract dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def rc = registerContractResourceService.update(dto)
            rc
        }
    }

    @GET
    Response readAll() {
        ok {
            def registerContract = registerContractResourceService.readAll()
            registerContract
        }
    }

    @POST
    @Path('/readUseForPage')
    Response readUseForPage(Finfo finfo) {
        page {
            def json = registerContractResourceService.readUseForPage(finfo.pagesize, finfo.startposition, finfo.keyword)
            return [data: json.get("page"), total: json.get("size")]
        }
    }

    @POST
    @Path('/readReturnForPage')
    Response readReturnForPage(Finfo finfo) {
        page {

            def json = registerContractResourceService.readReturnForPage(finfo.pagesize, finfo.startposition, finfo.keyword)

            return [data: json.get("page"), total: json.get("size")]
        }

    }

    @Path('/{id}')
    RegisterContractResource getResource(@PathParam('id') Long id) {
        new RegisterContractResource(registerContractResourceService: registerContractResourceService, id: id)
    }

    /**
     * 合同领用操作
     * @param dto
     * @return
     */
    @POST
    @Path('/useContract')
    Response useContract(RegisterContract dto) {
        ok {
            validBh(dto.startNum)
            validBh(dto.endNum)
            dto.actionType = false
            def fund = dto.fund
            int sn = Integer.parseInt(dto.startNum.substring(nameLength))
            int en = Integer.parseInt(dto.endNum.substring(nameLength))
            def jc = []
            for (int i = sn; i <= en; i++) {
                Contract c = Contract.findBySzbhAndSflyAndFund(i, false, fund)
                if (null == c) {
                    throw new Exception("合同编号范围不正确!")
                    break

                } else {
                    print("put " + i)
                    jc.push(c)
                }
            }
            jc.each {
                Contract d = it
                d.sfly = true
                d.save(failOnError: true)
            }

            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
            def rc = registerContractResourceService.create(dto)
            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
            rc.save(failOnError: true)

            return rc
        }

    }

    boolean validBh(String qsbh) {
        print(nameLength)
        print("起始编号:应为" + nameLength + "位字母加" + numberLength + "位序号")
        if (qsbh == null || qsbh.length() != nameLength + numberLength) {
            throw new Exception("\"编号\"应为" + nameLength + "位字母加" + numberLength + "位序号")
        }
        return true
    }
    //归还操作
    @POST
    @Path('/returnContract')
    Response returnContract(RegisterContract dto) {
        ok {
            validBh(dto.startNum)
            validBh(dto.endNum)
            def fund = dto.fund
            dto.actionType = true
            dto.department=   dto.receiveUser.department
            int sn = Integer.parseInt(dto.startNum.substring(nameLength))
            int en = Integer.parseInt(dto.endNum.substring(nameLength))
            def jc = []
            for (int i = sn; i <= en; i++) {
                Contract c = Contract.findBySzbhAndSflyAndFund(i, true, fund)
                if (null == c) {
                    throw new Exception("合同编号范围不正确!")
                    break
                } else {
                    jc.push(c)
                }
            }
            jc.each {
                Contract d = it
                d.sfly = false
                d.save(failOnError: true)
            }
            StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("F"))
            dto.indexNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
            def rc = registerContractResourceService.create(dto)
            rc.indexNum = CreateNumberService.getFullNumber(former, dto.id.toString())
            rc.save(failOnError: true)
            return rc
        }
    }
}