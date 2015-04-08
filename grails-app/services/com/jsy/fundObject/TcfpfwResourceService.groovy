package com.jsy.fundObject

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class TcfpfwResourceService {

    def create(Tcfpfw dto) {
        dto.save()
    }

    def read(id) {
        def obj = Tcfpfw.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Tcfpfw.class, id)
        }
        obj
    }

    def readAll() {
        Tcfpfw.findAll()
    }

    def update(Tcfpfw dto) {
        def obj = Tcfpfw.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Tcfpfw.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Tcfpfw.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
