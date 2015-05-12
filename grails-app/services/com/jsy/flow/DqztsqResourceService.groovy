package com.jsy.flow

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.utility.CreateInvestmentArchivesService
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class DqztsqResourceService {

    CreateInvestmentArchivesService createInvestmentArchivesService

    def create(Dqztsq dto) throws Exception {
        InvestmentArchives investmentArchives = InvestmentArchives.get(dto.oldArchivesId)
        investmentArchives.dazt = INVESTMENT_SPEICAL_STATUS.DQZT.value
        investmentArchives.status = INVESTMENT_STATUS.New.value
        investmentArchives.save(failOnError: true)
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = Dqztsq.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Dqztsq.class, id)
        }
        obj
    }

    def readAll() {
        Dqztsq.findAll()
    }

    def update(Dqztsq dto) {
        def obj = Dqztsq.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Dqztsq.class, dto.id)
        }
        obj.union(dto)
        obj
    }

    void delete(id) {
        def obj = Dqztsq.get(id)
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
//        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
//        def c = Customer.findAllByNameLike("%" +queryparam+"%")
//        json.put("page", Dqztsq.findAllByZtjjInListOrFundNameLikeOrHtbhLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [max: pagesize, offset: startposition]))
//        json.put("size", Dqztsq.findAllByZtjjInListOrFundNameLikeOrHtbhLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c).size())
        json.put("page", Dqztsq.findAllByFundNameLikeOrHtbhLike("%"+queryparam+"%", "%"+queryparam+"%" , [max: pagesize, offset: startposition]))
        json.put("size", Dqztsq.findAllByFundNameLikeOrHtbhLike("%"+queryparam+"%", "%"+queryparam+"%" ).size())

        return  json

    }

    //到期转投申请业务处理
    def dqztcl(Long id){
        Dqztsq dqztsq=Dqztsq.get(id)
        InvestmentArchives oldInv=InvestmentArchives.get(dqztsq.oldArchivesId)
        oldInv.bj=oldInv.tzje-dqztsq.ztje
        oldInv.status=2
        oldInv.dazt=1
        oldInv.save(failOnError: true)
        //新建档案
        return createInvestmentArchivesService.create(dqztsq.ztjj,oldInv,dqztsq.ztje,dqztsq.xhtbh,dqztsq.rgrq)
    }

}
