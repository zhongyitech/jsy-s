package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.utility.CreateInvestmentArchivesService
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class WdqztsqResourceService {
    CreateInvestmentArchivesService createInvestmentArchivesService

    def create(Wdqztsq dto) {
        dto.save(failOnError: true)
        return dto
    }

    def read(id) {
        def obj = Wdqztsq.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Wdqztsq.class, id)
        }
        obj
    }

    def readAll() {
        Wdqztsq.findAll()
    }

    def update(Wdqztsq dto) {
        def obj = Wdqztsq.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Wdqztsq.class, dto.id)
        }
        obj.union(dto)
        obj
    }

    void delete(id) {
        def obj = Wdqztsq.get(id)
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
        json.put("page", Wdqztsq.findAllByFundNameLikeOrHtbhLike( "%"+queryparam+"%", "%"+queryparam+"%", [max: pagesize, offset: startposition]))
        json.put("size", Wdqztsq.findAllByFundNameLikeOrHtbhLike( "%"+queryparam+"%", "%"+queryparam+"%").size())

        return  json

    }

    //未到期转投申请业务处理
    def wdqztcl(Long id){
        Wdqztsq wdqztsq=Wdqztsq.get(id)
        InvestmentArchives oldInv=InvestmentArchives.get(wdqztsq.oldArchivesId)
        oldInv.bj=oldInv.tzje-wdqztsq.ztje-wdqztsq.kcwyj-wdqztsq.ywtchsje-wdqztsq.gltchsje
        oldInv.status=2
        oldInv.dazt=2
        oldInv.save(failOnError: true)

        //新建档案
        return createInvestmentArchivesService.create(wdqztsq.ztjj,oldInv,wdqztsq.ztje,wdqztsq.xhtbh,wdqztsq.rgrq)
    }

}
