package com.jsy.project

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class ReceiveRecordResourceService {

    def create(ReceiveRecord dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = ReceiveRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ReceiveRecord.class, id)
        }
        obj
    }

    def readAll() {
        ReceiveRecord.findAll()
    }

    def update(ReceiveRecord dto) {
        def obj = ReceiveRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ReceiveRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ReceiveRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
