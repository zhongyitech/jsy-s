package com.jsy.flow

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.utility.CreateInvestmentArchivesService
import com.jsy.utility.GetYieldService
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import com.jsy.utility.MyException
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class JjxtsqResourceService {
    CreateInvestmentArchivesService createInvestmentArchivesService

    def create(Jjxtsq dto) {
        InvestmentArchives investmentArchives = InvestmentArchives.get(dto.oldArchivesId)
        investmentArchives.dazt = INVESTMENT_SPEICAL_STATUS.JJXT.value
        investmentArchives.status = INVESTMENT_STATUS.New.value
        investmentArchives.save(failOnError: true)
        dto.save(failOnError: true)
        def cpr=ContractPredistribution.addRow(dto.xhtbh,dto.guid)
        cpr.save(failOnError: true)
    }

    def read(id) {
        def obj = Jjxtsq.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Jjxtsq.class, id)
        }
        obj
    }

    def readAll() {
        Jjxtsq.findAll()
    }

    def update(Jjxtsq dto) {
        def obj = Jjxtsq.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Jjxtsq.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Jjxtsq.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
        if (null == queryparam){

            queryparam = ""
        }
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        json.put("page", Jjxtsq.findAllByFundNameLikeOrHtbhLike( "%"+queryparam+"%", "%"+queryparam+"%", [max: pagesize, offset: startposition]))
        json.put("size", Jjxtsq.findAllByFundNameLikeOrHtbhLike( "%"+queryparam+"%", "%"+queryparam+"%").size())

        return  json

    }

    //基金续投申请业务处理
    def jjxtcl(Long id){
        Jjxtsq jjxtsq=Jjxtsq.get(id)
        InvestmentArchives oldInv=InvestmentArchives.get(jjxtsq.oldArchivesId)
        oldInv.bj=oldInv.tzje-jjxtsq.xtbje
        oldInv.status=2
        oldInv.dazt=3
        oldInv.save(failOnError: true)
        //新建档案
        return createInvestmentArchivesService.create(oldInv.fund,oldInv,jjxtsq.ztzje,jjxtsq.xhtbh,jjxtsq.rgrq)
    }

    /**
     * 用新的投资金额计算收益率数据
     * @param id
     * @param totalAmount
     * @return
     */
    def getNewInfo(long id,BigDecimal totalAmount){
        def iv = InvestmentArchives.get(id)
        if (iv == null)
            throw new MyException("投资档案ID号不正确!")
        println("contractNum:" + iv.contractNum)
        println("totalAmount:" + totalAmount)
        def ver = iv.contractNum.substring(3, 3 + 1)
        def yield = GetYieldService.getYield(iv.fund.id, iv.ywjl.department.leader.id, totalAmount, ver.toUpperCase())
        return [totalAmount: totalAmount, totalRate: yield.rest_yield, totalTzqx: iv.tzqx, muteLx: 0, totalFxfj: iv.fxfs]
    }

}
