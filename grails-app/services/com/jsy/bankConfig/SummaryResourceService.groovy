package com.jsy.bankConfig

import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class SummaryResourceService {

    def create(Summary dto) {
        dto.save(failOnError: true)
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
        def location = obj.indexlocation
        obj.properties = dto.properties
        obj.indexlocation = location
        obj.save(failOnError: true)
    }

    def upIndex(Long id) {
        def target = Summary.get(id)
        def upTarget = Summary.get(id + 1)
        if (upTarget != null) {
            def index = upTarget.indexlocation
            upTarget.indexlocation = target
            target.indexlocation = index
        }
        target
    }

    def downIndex(Long id) {
        def target = Summary.get(id)
        def upTarget = Summary.get(id - 1)
        if (upTarget != null) {
            def index = upTarget.indexlocation
            upTarget.indexlocation = target
            target.indexlocation = index
        }
        target
    }

    void delete(id) {
        def obj = Summary.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
