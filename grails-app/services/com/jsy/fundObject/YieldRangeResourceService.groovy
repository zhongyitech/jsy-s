package com.jsy.fundObject

import org.grails.jaxrs.provider.DomainObjectNotFoundException

class YieldRangeResourceService {

    def create(YieldRange dto) {
        dto.save()
    }

    def read(id) {
        def obj = YieldRange.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(YieldRange.class, id)
        }
        obj
    }

    def readAll() {
        YieldRange.findAll()
    }

    def update(YieldRange dto) {
        def obj = YieldRange.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(YieldRange.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = YieldRange.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
