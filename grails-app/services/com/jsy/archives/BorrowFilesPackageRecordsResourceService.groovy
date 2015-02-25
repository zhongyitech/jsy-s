package com.jsy.archives

import com.jsy.auth.User
import com.jsy.system.TypeConfig
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class BorrowFilesPackageRecordsResourceService {

    def create(BorrowFilesPackageRecords dto) {
        if(dto.filePackage.borrowstatus.mapValue==1){
            dto.filePackage.borrowstatus = TypeConfig.findByTypeAndMapValue(5,2)//11为在库
            dto.filePackage.save();
            dto.returned=false
            return dto.save(failOnError: true)
        }else{
            throw new Exception("已借出！")
        }
    }

    def read(id) {
        def obj = BorrowFilesPackageRecords.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(BorrowFilesPackageRecords.class, id)
        }
        obj
    }

    def readAll() {
        BorrowFilesPackageRecords.findAll()
    }

    def update(BorrowFilesPackageRecords dto) {
        def obj=BorrowFilesPackageRecords.findByFilePackageAndReturned(dto.filePackage,false);

        if (!obj) {
            throw new DomainObjectNotFoundException(BorrowFilesPackageRecords.class, dto.id)
        }
        dto.filePackage.borrowstatus = TypeConfig.findByTypeAndMapValue(5,1)//11为在库
        dto.filePackage.save();
        //obj.properties = dto.properties
        obj.returnTime=dto.returnTime
        obj.returnRemark=dto.returnRemark
        obj.returned=true
        obj.save(failOnError: true)
    }

    void delete(id) {
        def obj = BorrowFilesPackageRecords.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        if (null == queryparam){
            queryparam = ""
        }
        JSONObject json = new JSONObject()

        json.put("page", BorrowFilesPackageRecords.list(max: pagesize, offset: startposition))
        json.put("size", BorrowFilesPackageRecords.list().size())

        return  json

    }


}
