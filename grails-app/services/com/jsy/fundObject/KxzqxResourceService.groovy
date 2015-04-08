package com.jsy.fundObject

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class KxzqxResourceService {

    def create(Kxzqx dto) {
        dto.save()
    }

    def read(id) {
        def obj = Kxzqx.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Kxzqx.class, id)
        }
        obj
    }

    def readAll() {
        Kxzqx.findAll()
    }

    def update(Kxzqx dto) {
        def obj = Kxzqx.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Kxzqx.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Kxzqx.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
