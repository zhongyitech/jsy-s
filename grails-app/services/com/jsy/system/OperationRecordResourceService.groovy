package com.jsy.system

import com.jsy.utility.DomainHelper
import org.grails.jaxrs.provider.DomainObjectNotFoundException


class OperationRecordResourceService {

    def create(OperationRecord dto) {
        dto.save()
    }

    def read(id) {
        def obj = OperationRecord.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(OperationRecord.class, id)
        }
        obj
    }

    def readAll() {
        OperationRecord.findAll()
    }

    def update(OperationRecord dto) {
        def obj = OperationRecord.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(OperationRecord.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = OperationRecord.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(int pagesize, int offset, def query) {
        def dc = DomainHelper.getDetachedCriteria(OperationRecord, query)
        return [data: dc.list([max: pagesize,offset: offset]),total:pagesize == 0 ? 0 : dc.count()]
    }
}
