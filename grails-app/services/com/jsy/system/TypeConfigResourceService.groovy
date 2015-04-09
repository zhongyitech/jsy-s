package com.jsy.system

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class TypeConfigResourceService {

    def create(TypeConfig dto) {
        dto.save()
    }

    def read(id) {
        def obj = TypeConfig.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(TypeConfig.class, id)
        }
        obj
    }

    def readAll() {
        TypeConfig.findAll()
    }

    def update(TypeConfig dto) {
        def obj = TypeConfig.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(TypeConfig.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = TypeConfig.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def findByType(int type) {
        TypeConfig.findAllByType(type)
    }

}
