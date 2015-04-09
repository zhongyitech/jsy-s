package com.jsy.system

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class UploadFileResourceService {

    def create(UploadFile dto) {
        dto.save()
    }

    def read(id) {
        def obj = UploadFile.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(UploadFile.class, id)
        }
        obj
    }

    def readAll() {
        UploadFile.findAll()
    }

    def update(UploadFile dto) {
        def obj = UploadFile.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(UploadFile.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = UploadFile.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
