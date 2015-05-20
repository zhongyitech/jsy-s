package com.jsy.flow

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import com.jsy.utility.SpecialFlow
import grails.converters.JSON
import org.json.JSONObject

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

@Path('/api/jjxtsq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class JjxtsqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    JjxtsqResourceService jjxtsqResourceService
    def springSecurityService

    @POST
    Response create(Jjxtsq dto) {
        ok {

            def iv = InvestmentArchives.findByContractNum(dto.htbh)
            //检测旧档案是否能做特殊申请操作
            SpecialFlow.Create.Validation(iv)
            dto.scrq = new Date()
            dto.sqr = springSecurityService.getCurrentUser()
            dto.sqbm = dto.sqr.department ? dto.sqr.department.deptName : ""
            def jj = jjxtsqResourceService.create(dto)

            jj
        }
    }

    @GET
    Response getById(@QueryParam('id') Long id) {
        ok {
            def wd = Jjxtsq.findById(id)
            wd
        }
    }

    @PUT
    Response update(Jjxtsq dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def wd = jjxtsqResourceService.update(dto)
            wd
        }
    }

    @Path('/{id}')
    JjxtsqResource getResource(@PathParam('id') Long id) {
        new JjxtsqResource(jjxtsqResourceService: jjxtsqResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(Jjxtsq, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
        }

    }

}
