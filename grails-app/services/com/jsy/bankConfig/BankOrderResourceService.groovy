package com.jsy.bankConfig

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class BankOrderResourceService {

    def create(BankOrder dto) {
        dto.save()
    }

    def read(id) {
        def obj = BankOrder.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankOrder.class, id)
        }
        obj
    }

    def readAll() {
        BankOrder.findAll()
    }

    def update(BankOrder dto) {
        def obj = BankOrder.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankOrder.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = BankOrder.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
