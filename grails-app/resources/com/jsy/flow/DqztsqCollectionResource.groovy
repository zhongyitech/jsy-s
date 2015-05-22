package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.utility.DomainHelper
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import com.jsy.utility.MyException
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
    DqztsqResourceService dqztsqResourceService
    def springSecurityService

    @POST
    Response create(Dqztsq dto) {
        ok {
            //todo:生成特殊申请单的唯一编号

            def iv = InvestmentArchives.findByContractNum(dto.htbh)
            //检测旧档案是否能做特殊申请操作
            SpecialFlow.Create.Validation(iv)
            def fund = Contract.findByHtbh(dto.xhtbh)?.fund
            if (fund != null) {
                if (fund.id == iv.fund.id) {
                    throw new MyException("新合同编号对应的基金与原档案相同,不能进行转投!")
                }
            }
            dto.sqrq = new Date()
            dto.sqr = springSecurityService.getCurrentUser()
            dto.sqbm = dto.sqr.department ? dto.sqr.department.deptName : ""
            def dd = dqztsqResourceService.create(dto)
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
            def sType = arg.sq_type
            String sType_descript = ""
            DetachedCriteria dc = null
            def data = []
            switch (sType) {
            //1.委托付款申请
                case "1"://"Wtfksq":
                    dc = DomainHelper.getDetachedCriteria(Wtfksq, arg)
                    sType_descript = "委托付款申请"
                    break
            //2.到期转投申请
                case "2"://"Dqztsq":
                    dc = DomainHelper.getDetachedCriteria(Dqztsq, arg)
                    sType_descript = "到期转投申请"

                    break
            //3.未到期转换申请
                case "3"://"Wdqztsq":
                    dc = DomainHelper.getDetachedCriteria(Wdqztsq, arg)
                    sType_descript = "未到期转换申请"

                    break
            //4.基金续投
                case "4"://"Jjxtsq":
                    dc = DomainHelper.getDetachedCriteria(Jjxtsq, arg)
                    sType_descript = "基金续投"

                    break
            //5.退伙
                case "5"://"Thclsq":
                    dc = DomainHelper.getDetachedCriteria(Thclsq, arg)
                    sType_descript = "退伙"

                    break
                //合并申请单
                case "6":// "UnionSpecial":
                    dc = DomainHelper.getDetachedCriteria(Mergesq, arg)
                    sType_descript = "合并申请"
                    break
                default:
                    break
            }
            if (dc != null) {
                data = dc.list([max: arg.pagesize, offset: arg.startposition]).collect {
                    def row = [id: it.id]
                    row.putAll(it.properties)
                    row.put("sType", sType_descript)
                    row.put("sq_type", sType)
                    row.put("customer", [name: it.customer.name])
                    row.put("sqr", [chainName: it.sqr.chainName])
                    return row
                }
                return [data: data, total: dc.count()]
            }
        }
    }
}
