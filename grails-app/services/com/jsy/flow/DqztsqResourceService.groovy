package com.jsy.flow

import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.utility.CreateInvestmentArchivesService
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import com.jsy.utility.MyException
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

    def readAllForPage(Long pagesize, Long startposition, String queryparam) {
        JSONObject json = new JSONObject()
        if (null == queryparam) {

            queryparam = ""
        }
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
//        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
//        def c = Customer.findAllByNameLike("%" +queryparam+"%")
//        json.put("page", Dqztsq.findAllByZtjjInListOrFundNameLikeOrHtbhLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [max: pagesize, offset: startposition]))
//        json.put("size", Dqztsq.findAllByZtjjInListOrFundNameLikeOrHtbhLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c).size())
        json.put("page", Dqztsq.findAllByFundNameLikeOrHtbhLike("%" + queryparam + "%", "%" + queryparam + "%", [max: pagesize, offset: startposition]))
        json.put("size", Dqztsq.findAllByFundNameLikeOrHtbhLike("%" + queryparam + "%", "%" + queryparam + "%").size())

        return json

    }

    //到期转投申请业务处理
    def dqztcl(Long id) {
        Dqztsq dqztsq = Dqztsq.get(id)
        InvestmentArchives oldInv = InvestmentArchives.get(dqztsq.oldArchivesId)
        oldInv.bj = oldInv.tzje - dqztsq.ztje
        oldInv.status = 2
        oldInv.dazt = 1
        oldInv.save(failOnError: true)
        //新建档案
        return createInvestmentArchivesService.create(dqztsq.ztjj, oldInv, dqztsq.ztje, dqztsq.xhtbh, dqztsq.rgrq)
    }

    /**
     * 验证特殊申请单是否可操作，1 不能为空  2.状态必须为0（待审核）
     * @param order
     */
    def vaildSpeicalCanCancel(def order) {
        if (order == null) throw new MyException("没有此特殊申请单！")
        if (order.status != 0) {
            throw new MyException("特殊申请单审核通过不能取消")
        }
    }

    /**
     * 取消特殊申请操作
     * @param id
     * @param stype
     */
    def cancelOrder(long id, Long stype) {
        switch (stype) {
            case 1:
                def dc = Wtfksq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = dc.archives
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.delete()

                break
            case 2:
                def dc = Dqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.delete()

                break
            case 3:
                def dc = Wdqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.delete()

                break
            case 4:
                def dc = Jjxtsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.delete()

                break
            case 5:
                def dc = Thclsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.delete()

                break
            case 6:
                def dc = Mergesq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.delete()
                break
            default:
                break
        }
    }

    /**
     * 同意特殊申请操作
     * @param id
     * @param stype
     */
    def acceptOrder(long id, Long stype) {
        switch (stype) {
            case 1:
                def dc = Wtfksq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = dc.archives
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.status = 1

                break
            case 2:
                def dc = Dqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)

                break
            case 3:
                def dc = Wdqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)

                break
            case 4:
                def dc = Jjxtsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)

                break
            case 5:
                def dc = Thclsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)

                break
            case 6:
                def dc = Mergesq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)


                break
            default:
                break
        }
    }
}
