package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.utility.DomainHelper
import com.jsy.utility.SpecialFlow

import javax.ws.rs.PUT
import javax.ws.rs.QueryParam
import static com.jsy.utility.MyResponse.*
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/thclsq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ThclsqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    ThclsqResourceService thclsqResourceService
    def springSecurityService

    @POST
    Response create(Thclsq dto) {
        ok {
            SpecialFlow.Create.Validation(InvestmentArchives.findByContractNum(dto.htbh))
            dto.scrq = new Date()
            dto.sqr = springSecurityService.getCurrentUser()
            dto.sqbm = dto.sqr.department ? dto.sqr.department.deptName : ""
            println(dto)
            def th = thclsqResourceService.create(dto)
            th
        }
    }

    @GET
    Response getById(@QueryParam('id') Long id) {
        ok {
            def wd = Thclsq.findById(id)
            wd
        }
    }

    @PUT
    Response update(Thclsq dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def wd = thclsqResourceService.update(dto)
            wd
        }
    }

    @Path('/{id}')
    ThclsqResource getResource(@PathParam('id') Long id) {
        new ThclsqResource(thclsqResourceService: thclsqResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(Thclsq, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
        }
    }
}
