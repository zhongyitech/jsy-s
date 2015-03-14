package com.jsy.archives

import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.fundObject.RegisterContract
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray

class InvestmentArchivesResourceService {

    def create(InvestmentArchives dto) {
        dto.save(failOnError: true)
    }

    def read(id) {
        def obj = InvestmentArchives.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(InvestmentArchives.class, id)
        }
        obj
    }

    def readAll() {
        InvestmentArchives.findAll()
    }

    def update(InvestmentArchives dto, int id) {
        def obj = InvestmentArchives.get(id)

//        obj?.payTimes.clear()
//        obj?.save(flush: true)
//        obj?.payTimes.each {
//            it.delete(flush: true)
//        }

        //付息时间新增
        List times=scfxsj(dto.rgrq,dto.tzqx,dto.fxfs)
        int i=1
        times.each {
            PayTime payTime=new PayTime(px: i,fxsj: it,sffx: false).save(failOnError: true)
            dto.addToPayTimes(payTime)
            i++
        }
        if (!obj) {
            throw new DomainObjectNotFoundException(InvestmentArchives.class, id)
        }
        obj.union(dto)
        obj.save(failOnError: true)
    }

    void delete(id) {
        def obj = InvestmentArchives.get(id)
        if (obj) {
            obj.delete()
        }
    }
    def readAllForPage(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
        def c = Customer.findAllByNameLike("%" +queryparam+"%")
        def d = InvestmentArchives.findAllByContractNumLike("%"+queryparam+"%")

        def page = InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [sort:"id", order:"desc",max: pagesize,  offset: startposition])
        def ja = new JSONArray()
        page.each {
            JSONObject JSOB =new JSONObject((it as JSON).toString());
            def cus=it.customer
            if (null == cus){
                JSOB.put("customer", "{}")
            }else {
                JSOB.put("customer", it.customer.properties)
            }
            ja.put(JSOB)
        }
        json.put("page", ja)
//        json.put("page", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [sort:"id", order:"desc",max: pagesize,  offset: startposition]))
        json.put("size", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c).size())

        return  json

    }

    def IAOutput(Long pagesize,Long startposition,String queryparam){
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if(null == queryparam){
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
        def c = Customer.findAllByNameLike("%" +queryparam+"%")
        def d = InvestmentArchives.findAllByContractNumLike("%"+queryparam+"%")
        json.put("page", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [max: pagesize, sort:"id", order:"desc", offset: startposition]))
        json.put("size", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c).size())
        InvestmentArchives.listOrderByCustomer()
        return  json

    }

    def findByParm(String queryparam){
        if(null == queryparam){
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" +queryparam+"%")
//        InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrUsernameLike(f, "%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%")
        def ia = InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrUsernameLike(f, "%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%")
        return ia
    }

    //根据付息起始时间、期限、付息方式，生成对应多个付息时间
    def scfxsj(Date startTime,String qx,String fxfs){
        List list=new ArrayList()
        //日历对象
        Calendar calendar = Calendar.getInstance();
        //设置当前日期
        calendar.setTime(startTime);
        //获取期限的数值
        Double t=Double.valueOf(qx.substring(0,qx.length()-1))
        if(fxfs=="N"){
            if(qx.contains("天")){
                calendar.add(Calendar.DATE, (int)t)
            }else if(qx.contains("年")){
                calendar.add(Calendar.MONTH,(int)(t*12))
            }
            list.add(calendar.getTime())
        }else if(fxfs=="J"){
            (1..(int)(t*4)).each {
                calendar.add(Calendar.MONTH,3)
                list.add(calendar.getTime())
            }
        }else if(fxfs=="B"){
            (1..(int)(t*2)).each {
                calendar.add(Calendar.MONTH,6)
                list.add(calendar.getTime())
            }
        }else if(fxfs=="Y"){
            (1..(int)t).each {
                calendar.add(Calendar.YEAR,1)
                list.add(calendar.getTime())
            }
        }else if(fxfs=="M") {
            (1..(int) t*12).each {
                calendar.add(Calendar.MONTH, 1)
                list.add(calendar.getTime())
            }
        }
        return list
    }
}
