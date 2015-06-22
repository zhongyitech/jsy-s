package com.jsy.fundObject

import com.jsy.system.TypeConfig
import com.jsy.utility.CreateNumberService
import com.jsy.utility.MyException
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class FundResourceService {

    def seachByCriteria(String query, Map map) {
        return Fund.findAll("from Fund where " + query, map)
//        String s=""
//        def properties=jsonObject.get("properties")
//        def c = Fund.createCriteria()
//        for(int i=0;i<properties.size();i++){
//
//        }
//        def results = c {
//            between("balance", 500, 1000)
//            eq("branch", "London")
//            or {
//                like("holderFirstName", "Fred%")
//                like("holderFirstName", "Barney%")
//            }
//            maxResults(10)
//            order("holderLastName", "desc")
//        }
    }

    def create(Fund dto) {
        dto.save(failOnError: true)
    }

    def create(Fund dto,StringBuffer former) {
        dto.save(failOnError: true)
        dto.fundNo = CreateNumberService.getFullNumber(former, dto.id.toString())
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = Fund.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(Fund.class, id)
        }
        obj
    }

    def readAll() {
        Fund.findAll()

    }

//    读取主页面数据。。。
    def readMainPage(Long pagesize, Long startposition, String keyword, int status, Date startSaleDate1, Date startSaleDate2) {
        //参数：pagesize 每页数据条数
        //      startposition,查询起始位置
        JSONObject json = new JSONObject()
        List<Fund> fundList
        print("pagesize" + pagesize)
        print("startposition" + startposition)
        print("keyword" + keyword)
        print("status" + status)
        print("startSaleDate1" + startSaleDate1)
        print("startSaleDate2" + startSaleDate2)
        if (null == keyword) {
            keyword = ""
        }
//        List funds = null
//        if(start == null && end ==null){
//            funds = Fund.findAll();
//        }else if(start == null){
//            funds = Fund.findAllB
//        }
//        }else if(end == null){
//
//        }


        if (status == 200) {
            print("find without status")
//            fundList = Fund.findAllByFundNameLikeAndStartSaleDateBetween("%"+keyword+"%", startSaleDate1, startSaleDate2,[max: pagesize, offset: startposition])
//            json.put("page", Fund.findAllByFundNameLikeAndIdInList("%"+keyword+"%", funds,[max: pagesize, offset: startposition]))
            json.put("page", Fund.findAllByFundNameLikeAndStartSaleDateBetween("%" + keyword + "%", startSaleDate1, startSaleDate2, [max: pagesize, offset: startposition]))
            print("page.objece: " + Fund.findAllByFundNameLikeAndStartSaleDateBetween("%" + keyword + "%", startSaleDate1, startSaleDate2, [max: pagesize, offset: startposition]).size())
            json.put("size", Fund.findAllByFundNameLikeAndStartSaleDateBetween("%" + keyword + "%", startSaleDate1, startSaleDate2).size())


        } else {
            print("find with status = " + status)
            def s = TypeConfig.get(status)
//            fundList = Fund.findAllByFundNameLikeAndStatus("%"+keyword+"%", status, startSaleDate1, startSaleDate2,[max: pagesize, offset: startposition])
//            json.put("page", Fund.findAllByFundNameLikeAndStatus("%"+keyword+"%", status, startSaleDate1, startSaleDate2,[max: pagesize, offset: startposition]))
//            json.put("size", Fund.findAllByFundNameLikeAndStatus("%"+keyword+"%", status, startSaleDate1, startSaleDate2).size())
            json.put("page", Fund.findAllByFundNameLikeAndStatusAndStartSaleDateBetween("%" + keyword + "%", s, startSaleDate1, startSaleDate2, [max: pagesize, offset: startposition]))
            print("page.objece: " + Fund.findAllByFundNameLikeAndStatusAndStartSaleDateBetween("%" + keyword + "%", s, startSaleDate1, startSaleDate2, [max: pagesize, offset: startposition]).size())
            json.put("size", Fund.findAllByFundNameLikeAndStatusAndStartSaleDateBetween("%" + keyword + "%", s, startSaleDate1, startSaleDate2).size())
        }
        return json
    }


    def update(Fund dto, def id) {
        def obj = Fund.get(id)
        obj.tcfpfw.each {
            it.delete()
        }
        if (!obj) {
            throw new DomainObjectNotFoundException(Fund.class, dto.id)
            return false
        }
        obj.union(dto)
        obj

    }

    def delete(def id) {
        def obj = Fund.get(id)
        if (obj) {
            obj.delete()
            return true
        } else {
            return false;
        }
    }

    void checkInvestmentAmount(Fund fund, BigDecimal tzje) {
        if(fund==null )
            throw new MyException("基金ID不正确！")
        if (fund.limitRules == 0) {
            if (tzje < fund.minInvestmentAmount) {
                throw new MyException("投资金额不满足基金（" + fund.fundName + "+）的最低投资额(" + fund.minInvestmentAmount + ")要求！")
            }
        }
        if (fund.limitRules == 1) {
            if (tzje % fund.minInvestmentAmount != 0) {
                throw new MyException("投资金额不满足基金（" + fund.fundName + "+）的最低投资额(" + fund.minInvestmentAmount + "X)整数倍要求！")
            }
        }
    }
}
