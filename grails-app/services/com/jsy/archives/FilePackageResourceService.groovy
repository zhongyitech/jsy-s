package com.jsy.archives

import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class FilePackageResourceService {

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()
        json.put("page", FilePackage.findAllByFpnoLikeOrFpcodeLikeOrInformationLikeOrCreatorLike("%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%",  [max: pagesize, sort: "id", order: "desc", offset: startposition]))
        json.put("size", FilePackage.findAllByFpnoLikeOrFpcodeLikeOrInformationLikeOrCreatorLike("%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%","%"+queryparam+"%").size())

        return  json

    }

    def create(FilePackage dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = FilePackage.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FilePackage.class, id)
        }
        obj
    }

    def readAll() {
        FilePackage.findAll()
    }

    def update(FilePackage dto) {
        def obj = FilePackage.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(FilePackage.class, dto.id)
        }
        obj.union(dto)
        obj
    }

    void delete(id) {
        def obj = FilePackage.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
