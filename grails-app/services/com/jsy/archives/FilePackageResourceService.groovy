package com.jsy.archives

import com.jsy.utility.ContractFlow
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

import java.util.concurrent.ExecutionException

@Transactional(rollbackFor = Throwable.class)
class FilePackageResourceService {

    def readAllForPage(Long pagesize, Long startposition, String queryparam) {
        if (null == queryparam) {
            queryparam = ""
        }
        JSONObject json = new JSONObject()
        json.put("page", FilePackage.findAllByFpnoLikeOrFpcodeLikeOrInformationLikeOrCreatorLike("%" + queryparam + "%", "%" + queryparam + "%", "%" + queryparam + "%", "%" + queryparam + "%", [max: pagesize, sort: "id", order: "desc", offset: startposition]))
        json.put("size", FilePackage.findAllByFpnoLikeOrFpcodeLikeOrInformationLikeOrCreatorLike("%" + queryparam + "%", "%" + queryparam + "%", "%" + queryparam + "%", "%" + queryparam + "%").size())

        return json

    }

    def create(FilePackage dto) {
        ContractFlow.InputFilePackage.ValidationNum(dto.contractNum)
        def iv = InvestmentArchives.findByContractNum(dto.contractNum)
        dto.save(failOnError: true)
        iv.status = INVESTMENT_STATUS.Normal.value
        iv.save(failOnError: true)
        dto
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
        //不能更新合同编号字段
        //todo:添加其它的限制
        dto.contractNum = null
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
