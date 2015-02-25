package com.jsy.bankConfig

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class SummaryResourceService {

    def create(Summary dto) {
        dto.save()
    }

    def read(id) {
        def obj = Summary.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Summary.class, id)
        }
        obj
    }

    def readAll() {
        Summary.findAll()
    }

    def update(Summary dto) {
        def obj = Summary.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Summary.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = Summary.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
