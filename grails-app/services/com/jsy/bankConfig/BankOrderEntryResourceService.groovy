package com.jsy.bankConfig

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class BankOrderEntryResourceService {

    def create(BankOrderEntry dto) {
        dto.save()
    }

    def read(id) {
        def obj = BankOrderEntry.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankOrderEntry.class, id)
        }
        obj
    }

    def readAll() {
        BankOrderEntry.findAll()
    }

    def update(BankOrderEntry dto) {
        def obj = BankOrderEntry.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BankOrderEntry.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = BankOrderEntry.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
