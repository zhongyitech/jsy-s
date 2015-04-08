package com.jsy.flow

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class WtfksqResourceService {

    def create(Wtfksq dto) {
        dto.save()
    }

    def read(id) {
        def obj = Wtfksq.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Wtfksq.class, id)
        }
        obj
    }

    def readAll() {
        Wtfksq.findAll()
    }

    def update(Wtfksq dto) {
        def obj = Wtfksq.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Wtfksq.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Wtfksq.get(id)
        if (obj) {
            obj.delete()
        }
    }

    //委托付款申请业务处理
    def wtfkcl(Long id){
        Wtfksq wtfksq=Wtfksq.get(id)
        wtfksq.khfbs.each {
            new Wtfkjl(archivesId:wtfksq.archivesId,fkje:it.fkje,fkrq:it.fkrq,type:it.type,name:it.name,fddbr:it.fddbr,zjhm:it.zjhm).save(failOnError: true)
        }
    }

}
