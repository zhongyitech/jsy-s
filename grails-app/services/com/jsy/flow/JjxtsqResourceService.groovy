package com.jsy.flow

import com.jsy.archives.InvestmentArchives
import com.jsy.utility.CreateInvestmentArchivesService
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class JjxtsqResourceService {
    CreateInvestmentArchivesService createInvestmentArchivesService

    def create(Jjxtsq dto) {
        dto.save(failOnError: true)
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

}
