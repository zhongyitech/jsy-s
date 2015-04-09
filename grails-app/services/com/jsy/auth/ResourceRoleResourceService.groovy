package com.jsy.auth

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class ResourceRoleResourceService {

    def create(ResourceRole dto) {
        dto.save()
    }

    def read(id) {
        def obj = ResourceRole.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ResourceRole.class, id)
        }
        obj
    }

    def readAll() {
        ResourceRole.findAll()
    }

    def update(ResourceRole dto) {
        def obj = ResourceRole.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ResourceRole.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ResourceRole.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
