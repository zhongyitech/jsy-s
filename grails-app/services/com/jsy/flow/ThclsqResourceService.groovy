package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.archives.PaymentInfo
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class ThclsqResourceService {

    def create(Thclsq dto) {
        dto.save(failOnError: true)
        return  dto
    }

    def read(id) {
        def obj = Thclsq.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Thclsq.class, id)
        }
        obj
    }

    def readAll() {
        Thclsq.findAll()
    }

    def update(Thclsq dto) {
        def obj = Thclsq.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Thclsq.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Thclsq.get(id)
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
        json.put("page", Thclsq.findAllByFundNameLikeOrHtbhLike( "%"+queryparam+"%", "%"+queryparam+"%", [max: pagesize, offset: startposition]))
        json.put("size", Thclsq.findAllByFundNameLikeOrHtbhLike( "%"+queryparam+"%", "%"+queryparam+"%").size())

        return  json

    }

    //退伙申请业务处理
    def thsqcl(Long id){
        Thclsq thclsq=Thclsq.get(id)
        InvestmentArchives oldInv=InvestmentArchives.get(thclsq.oldArchivesId)
        oldInv.bj=oldInv.bj-thclsq.kcwyj-thclsq.ywtchsje-thclsq.gltchsje
        oldInv.status=2
        oldInv.dazt=4
        oldInv.save(failOnError: true)
        //生成本金兑付
        PaymentInfo paymentInfo = new PaymentInfo()
        paymentInfo.archivesId = oldInv.id
        paymentInfo.fxsj = new Date()
        paymentInfo.fundName = oldInv.fund.fundName
        paymentInfo.htbh = oldInv.archiveNum
        paymentInfo.customerName = thclsq.skr
        paymentInfo.tzje = oldInv.tzje
        paymentInfo.tzqx = oldInv.tzqx
        paymentInfo.syl = oldInv.nhsyl
        paymentInfo.bmjl = oldInv.bmjl.chainName
        paymentInfo.khh = thclsq.khh
        paymentInfo.yhzh = thclsq.yhzh
        paymentInfo.gj = oldInv.customer.country
        paymentInfo.zjlx = oldInv.customer.credentialsType
        paymentInfo.zjhm = oldInv.customer.credentialsNumber
        paymentInfo.yflx = 0
        paymentInfo.yfbj = oldInv.bj
        paymentInfo.zj = oldInv.bj
        paymentInfo.save(failOnError: true)
    }


}
