package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.utility.DomainHelper
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import com.jsy.utility.SpecialFlow
import grails.gorm.DetachedCriteria
import org.grails.jaxrs.provider.DomainObjectNotFoundException

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
    def springSecurityService

    @POST
    Response create(Dqztsq dto) {
        ok {
            //检测旧档案是否能做特殊申请操作
            SpecialFlow.Create.Validation(InvestmentArchives.findByContractNum(dto.htbh))
            dto.sqrq = new Date()
            dto.sqr = springSecurityService.getCurrentUser()
            dto.sqbm = dto.sqr.department ? dto.sqr.department.deptName : ""
            def dd = dqztsqResourceService.create(dto)
            InvestmentArchives investmentArchives = InvestmentArchives.get(dto.oldArchivesId)
            investmentArchives.dazt = INVESTMENT_SPEICAL_STATUS.DQZT.value
            investmentArchives.status = INVESTMENT_STATUS.New.value
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

            def list = dc.list([max: arg.pagesize, offset: arg.startposition])
            def customers = []

            list?.each {
                def temp = [:];
                it.properties.each { property ->
                    temp.put(property.key, property.value)
                }
                temp.customerName = it.customer.name
                temp.sqrName = it.sqr.chainName
                //状态 0,待审核 1,审核通过
                if (it.status == 0) {
                    temp.statusName = "待审核"
                } else if (it.status == 1) {
                    temp.statusName = "审核通过"
                } else {
                    temp.statusName = "未知"
                }

                customers << temp
            }
            //按分页要求返回数据格式 [数据,总页数]
            return [data: customers, total: arg.startposition == 0 ? dc.count() : 0, customers: customers]
        }
    }

    /**
     * 获取所有的特殊申请数据(包括其它类型)
     * @param arg
     * @return
     */
    @POST
    @Path('/getAll')
    Response getSpeicalAll(Map arg) {
        page {
            def sType = arg.sType
            DetachedCriteria dc = null
            def data = []
            switch (sType) {
            //1.委托付款申请
                case "Wtfksq":
                    dc = DomainHelper.getDetachedCriteria(Wtfksq, arg)
                    break
            //2.到期转投申请
                case "Dqztsq":
                    dc = DomainHelper.getDetachedCriteria(Dqztsq, arg)
                    data = dc.list([max: arg.pagesize, offset: arg.startposition]).collect {
                        def row = [id: it.id]
                        row.putAll(it.properties)
                        row.put("sType", "到期转投")
                        row.put("customer", [name: it.customer.name])
                        row.put("sqr", [chainName: it.sqr.chainName])
//                        row.put("department", it.sqr.department ? it.sqr.department.deptName : "")
                        return row
                    }
                    break
            //3.未到期转换申请
                case "Wdqztsq":
                    dc = DomainHelper.getDetachedCriteria(Wdqztsq, arg)
                    break
            //4.基金续投
                case "Jjxtsq":
                    dc = DomainHelper.getDetachedCriteria(Jjxtsq, arg)
                    break
            //5.退伙
                case "Thclsq":
                    dc = DomainHelper.getDetachedCriteria(Thclsq, arg)
                    break
                case "UnionSpecial":

                    break
                default:

                    break
            }
            if (dc != null) {
                return [data: data, total: dc.count()]
            }
        }
    }
}
