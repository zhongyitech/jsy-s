package com.jsy.project

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class PayRecordResourceService {

    def create(PayRecord dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = PayRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PayRecord.class, id)
        }
        obj
    }

    def readAll() {
        PayRecord.findAll()
    }

    def update(PayRecord dto) {
        def obj = PayRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PayRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = PayRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
