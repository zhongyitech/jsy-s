package com.jsy.flow
import com.jsy.archives.Contract
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

@Path('/api/dqztsq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class DqztsqCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def dqztsqResourceService

    @POST
    Response create(Dqztsq dto) {
        ok {
            //检测旧档案是否能做特殊申请操作
            SpecialFlow.Create.Validation(InvestmentArchives.findByContractNum(dto.htbh))

            dto.sqrq = new Date()
            def dd = dqztsqResourceService.create(dto)
            InvestmentArchives investmentArchives = InvestmentArchives.get(dto.oldArchivesId)
            investmentArchives.dazt = 1
            investmentArchives.status = 2
            investmentArchives.save(failOnError: true)
            dd
        }
    }

    @GET
    @Path('/getIAInfo')
    Response getIAInfo(@QueryParam('iaid') Long iaid) {
        ok {
            def ia = InvestmentArchives.get(iaid)
            ia
        }
    }

    @GET
    Response getById(@QueryParam('id') Long id) {
        ok {
            def wd = Dqztsq.findById(id)
            wd
        }
    }

    @PUT
    Response update(Dqztsq dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            def wd = dqztsqResourceService.update(dto)
            wd
        }
    }

    @Path('/{id}')
    DqztsqResource getResource(@PathParam('id') Long id) {
        new DqztsqResource(dqztsqResourceService: dqztsqResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(Dqztsq, arg)
            //todo: other code

            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
        }
    }
}
