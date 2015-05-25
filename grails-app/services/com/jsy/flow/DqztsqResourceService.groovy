package com.jsy.flow

import com.jsy.archives.Contract
import com.jsy.archives.INVESTMENT_STATUS
import com.jsy.archives.InvestmentArchives
import com.jsy.system.TypeConfig
import com.jsy.system.UploadFile
import com.jsy.utility.CreateInvestmentArchivesService
import com.jsy.utility.DateUtility
import com.jsy.utility.YieldService
import com.jsy.utility.INVESTMENT_SPEICAL_STATUS
import com.jsy.utility.MyException
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class DqztsqResourceService {
    CreateInvestmentArchivesService createInvestmentArchivesService
    YieldService yieldService

    def create(Dqztsq dto) throws Exception {
        InvestmentArchives investmentArchives = InvestmentArchives.get(dto.oldArchivesId)
        investmentArchives.dazt = INVESTMENT_SPEICAL_STATUS.DQZT.value
        investmentArchives.status = INVESTMENT_STATUS.New.value
        investmentArchives.save(failOnError: true)
        dto.save(failOnError: true)
        def cpr = ContractPredistribution.addRow(dto.xhtbh, dto.guid)
        cpr.save(failOnError: true)
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
                //删除预分配合同编号
                ContractPredistribution.findByGuid(dc.guid)?.delete()
                dc.delete()
                break
            case 3:
                def dc = Wdqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                ContractPredistribution.findByGuid(dc.guid)?.delete()
                dc.delete()

                break
            case 4:
                def dc = Jjxtsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                ContractPredistribution.findByGuid(dc.guid)?.delete()
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
                ContractPredistribution.findByGuid(dc.guid)?.delete()
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
        //委托付款
            case 1:
                def dc = Wtfksq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = dc.archives
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                dc.status = 1

                break
        //到期转投 TODO:
            case 2:
                def dc = Dqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def source = InvestmentArchives.get(dc.oldArchivesId)
                def newAmount = dc.ztje + dc.ztsye                                      //新档案的投资额=转投收益+转投金额
                /*   先获取新合同编号   */
                //1. 创建新的投资档案
                def dest = new InvestmentArchives()
                def newFund = Contract.findByHtbh(dc.xhtbh).fund
                //先全部复制
                dest.properties = source.properties
                //---处理特殊的数据
                dest.archiveNum = ""
                dest.description = "到期转投后的档案( $source.contractNum )"
                dest.id = null
                dest.fund = newFund
                dest.fundName = newFund.fundName
                dest.contractNum = dc.xhtbh
                dest.archiveFrom = source.id
                //新的认购日期
                dest.rgrq = DateUtility.lastDayWholePointDate(new Date())
                //设置投资金额
                dest.tzje = newAmount
                dest.uploadFiles = []
                source.uploadFiles.each {
                    def uf = new UploadFile()
                    uf.properties = it.properties
                    uf.id = null
                    dest.uploadFiles.add(uf)
                }
                dest.dycs = 0
                dest.zjdysj = null
                dest.sjtzje = dest.tzje
                yieldService.restSetTc(dest)
                yieldService.restGetYield(dest)
                yieldService.restPayTime(dest)
                dest.save(failOnError: true)

                //2. ------处理原投资档案------
                source.tzje = source.tzje - dc.ztje
                //设置原档案的本金额
                source.bj = source.tzje - dc.ztje
                def typec = TypeConfig.findByTypeAndMapValue(1, 4)
                source.htzt = typec      //存档

                /*------操作完成之后保存数据------*/
                source.status = INVESTMENT_STATUS.BackUp.value
                source.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                source.description = "已到期转投到$dest.contractNum"
                source.save(failOnError: true)
                dest.save(failOnError: true)
                //删除预分配数据
                ContractPredistribution.findByGuid(dc.guid)?.delete()
                dc.status = 1
//                throw new MyException("No Completed Method")
                break
        //未到期转投 TODO:
            case 3:
                def dc = Wdqztsq.get(id)
                vaildSpeicalCanCancel(dc)
                def source = InvestmentArchives.get(dc.oldArchivesId)
                def newAmount = dc.ztje                                     //新档案的投资额=转投收益+转投金额
                /*   先获取新合同编号   */
                //1. 创建新的投资档案
                def dest = new InvestmentArchives()
                def newFund = dc.ztjj
                //先全部复制
                dest.properties = source.properties
                //---处理特殊的数据
                dest.archiveNum = ""
                dest.description = "未到期转投后的档案( $source.contractNum )"
                dest.id = null
                dest.fund = newFund
                dest.fundName = newFund.fundName
                dest.contractNum = dc.xhtbh
                dest.archiveFrom = source.id
                //新的认购日期
                dest.rgrq = DateUtility.lastDayWholePointDate(new Date())
                //设置投资金额
                dest.tzje = newAmount
                dest.uploadFiles = []
                source.uploadFiles.each {
                    def uf = new UploadFile()
                    uf.properties = it.properties
                    uf.id = null
                    dest.uploadFiles.add(uf)
                }
                dest.dycs = 0
                dest.zjdysj = null
                dest.sjtzje = dest.tzje
                yieldService.restSetTc(dest)
                yieldService.restGetYield(dest)
                yieldService.restPayTime(dest)
                dest.save(failOnError: true)

                //2. ------处理原投资档案------
                source.tzje = source.tzje - dc.ztje
                //设置原档案的本金额
                source.bj = source.tzje - dc.ztje
                def typec = TypeConfig.findByTypeAndMapValue(1, 4)
                source.htzt = typec      //存档

                /*------操作完成之后保存数据------*/
                source.status = INVESTMENT_STATUS.BackUp.value
                source.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                source.description = "已未到期转投到：$dest.contractNum"
                source.save(failOnError: true)
                dest.save(failOnError: true)
                //删除预分配数据
                ContractPredistribution.findByGuid(dc.guid)?.delete()
                dc.status = 1
//                throw new MyException("No Completed Method")
                break
        //续投 TODO:
            case 4:
                def dc = Jjxtsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)

                break
        //退伙 TODO:
            case 5:
                def dc = Thclsq.get(id)
                vaildSpeicalCanCancel(dc)
                def iv = InvestmentArchives.get(dc.oldArchivesId)
                iv.status = INVESTMENT_STATUS.Normal.value
                iv.dazt = INVESTMENT_SPEICAL_STATUS.Normal.value
                iv.save(failOnError: true)
                break
        //合并 TODO:
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
