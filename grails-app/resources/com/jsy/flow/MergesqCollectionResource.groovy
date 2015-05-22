package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.utility.GetYieldService
import com.jsy.utility.MyException
import com.jsy.utility.MyResponse
import com.jsy.utility.SpecialFlow
import grails.plugin.springsecurity.SpringSecurityService

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/mergesq')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class MergesqCollectionResource {
    MergesqResourceService mergesqResourceService
    SpringSecurityService springSecurityService

    @POST
    @Path("/create")
    Response create(Mergesq dto) {
        MyResponse.ok {
            def iv = InvestmentArchives.findByContractNum(dto.htbh)
            //检测旧档案是否能做特殊申请操作
            SpecialFlow.Create.Validation(iv)
            if (dto.newContractNum == dto.htbh) {
                throw new MyException("合并入的档案编号与旧档案相同!")
            }
            if (dto.unionStartDate < new Date()) {
                throw new MyException("起息日期不能早于当前日期!", "unionStartDate")
            }
            def newIv = InvestmentArchives.findByContractNum(dto.newContractNum)
            if (newIv == null) throw new MyException("新合同编号没有对应的投资档案", "newContractNum")
            if (newIv.fund.id != iv.fund.id) throw new MyException("要合并入的投资档案与旧投资档案不是同一基金,不能合并", "newContractNum")
            try {
                //新档案是否能作为合并目标,没有做其它特殊操作
                SpecialFlow.Create.Validation(newIv)
            } catch (Exception ex) {
                throw new MyException("合同申请的目标档案:" + ex.message)
            }
            if (dto.real_lx < 0) throw new MyException("实际扣除利息不能小于0")
            dto.scrq = new Date()
            dto.sqr = springSecurityService.getCurrentUser()
            dto.sqbm = dto.sqr.department ? dto.sqr.department.deptName : ""
            dto.fundName = iv.fundName
            dto.totalAmount = dto.addAmount + iv.tzje
            dto.customer = iv.customer
            dto.xhtbh = dto.newContractNum
            def yield = GetYieldService.getYield(iv.fund.id, iv.ywjl.department.leader.id, dto.totalAmount, iv.contractNum.substring(3, 3 + 1).toUpperCase())
            //todo:重新计算的收益率
            dto.totalTzqx = iv.tzqx
            //todo:计算应扣除利息的数值
            dto.muteLx = 0
            dto.totalFxfj = iv.fxfs
            dto.totalRate = yield.rest_yield
            mergesqResourceService.create(dto)
        }
    }

    @POST
    @Path("/edit")
    Response edit(Mergesq dto) {
        ok {

        }
    }

    @GET
    Response readAll() {
        ok mergesqResourceService.readAll()
    }

    @Path('/{id}')
    MergesqResource getResource(@PathParam('id') Long id) {
        new MergesqResource(mergesqResourceService: mergesqResourceService, id: id)
    }

    /**
     * 重新计算投资档案的收益率
     * @param id 档案id
     * @param newAmount 追加的投资金额
     * @return 总投资额、新的年化率、投资期限、付息方式
     */
    @GET
    @Path('/unionPre')
    Response unionPre(@QueryParam("ivid") Long id, @QueryParam("newAmount") BigDecimal newAmount) {
        MyResponse.ok {
            def iv = InvestmentArchives.get(id)
            if (iv == null)
                throw new MyException("投资档案ID号不正确!")
            println("contractNum:" + iv.contractNum)
            def totalAmount = iv.tzje + newAmount
            println("totalAmount:" + totalAmount)
            def ver = iv.contractNum.substring(3, 3 + 1)
            def yield = GetYieldService.getYield(iv.fund.id, iv.ywjl.department.leader.id, newAmount + iv.tzje, ver.toUpperCase())
            return [totalAmount: iv.tzje + newAmount, totalRate: yield.rest_yield, totalTzqx: iv.tzqx, muteLx: 0, totalFxfj: iv.fxfs]
        }
    }

    public static int getIntervalDays(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return -1;
        }
        def odc = oDate.calendarDate
        oDate = new Date(odc.year, odc.month, odc.dayOfMonth)
        long intervalMilli = oDate.getTime() - fDate.getTime();
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }
}
