package com.jsy.archives

import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.fundObject.RegisterContract
import com.jsy.utility.InvestmentFlow
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException
import org.json.JSONArray

@Transactional(rollbackFor = Throwable.class)
class InvestmentArchivesResourceService {

    def create(InvestmentArchives dto) {
        InvestmentFlow.Create.Validation(dto)
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

    def update(InvestmentArchives dto, int id) throws Exception {
        def obj = InvestmentArchives.get(id)
        //判断是否可以进行更新操作
        InvestmentFlow.Update.Validation(obj)
        if(obj.status==2){
            throw new Exception("已归档，无法修改！")
        }
//        obj.customer.delete()
        // def did = obj.customer?.id
        try {
            obj?.payTimes.each {
                obj.removeFromPayTimes(it)
            }
        }catch (Exception ex){
            print(ex.message)
        }
        //付息时间新增

        List times = scfxsj(dto.rgrq, dto.tzqx, dto.fxfs)
        int i = 1
        times.each {
            PayTime payTime = new PayTime(px: i, fxsj: it, sffx: false, investmentArchives: obj).save(failOnError: true)
            obj.addToPayTimes(payTime)
            i++
        }
        if (!obj) {
            throw new DomainObjectNotFoundException(InvestmentArchives.class, id)
        }
        obj.union(dto)
        obj.save(failOnError: true)
        //  if (did) {
        //      Customer.get(did).delete()
        //   }
//        obj
    }

    void delete(id) {
        def obj = InvestmentArchives.get(id)
        if (obj) {
            obj.delete()
        }
    }

    def readAllForPage(Long pagesize, Long startposition, String queryparam) {
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if (null == queryparam) {
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" + queryparam + "%")
        def c = Customer.findAllByNameLike("%" + queryparam + "%")
        def d = InvestmentArchives.findAllByContractNumLike("%" + queryparam + "%")

        def page = InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%" + queryparam + "%", "%" + queryparam + "%", c, [sort: "id", order: "desc", max: pagesize, offset: startposition])
        def ja = new JSONArray()
        page.each {
            JSONObject JSOB = new JSONObject((it as JSON).toString());
            def cus = it.customer
            if (null == cus) {
                JSOB.put("customer", "{}")
            } else {
                JSOB.put("customer", it.customer.properties)
            }
            ja.put(JSOB)
        }
        json.put("page", ja)
//        json.put("page", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%"+queryparam+"%", "%"+queryparam+"%",c, [sort:"id", order:"desc",max: pagesize,  offset: startposition]))
        json.put("size", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%" + queryparam + "%", "%" + queryparam + "%", c).size())

        return json

    }

    def IAOutput(Long pagesize, Long startposition, String queryparam) {
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
        if (null == queryparam) {
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" + queryparam + "%")
        def c = Customer.findAllByNameLike("%" + queryparam + "%")
        def d = InvestmentArchives.findAllByContractNumLike("%" + queryparam + "%")
        json.put("page", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%" + queryparam + "%", "%" + queryparam + "%", c, [max: pagesize, sort: "id", order: "desc", offset: startposition]))
        json.put("size", InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrCustomerInList(f, "%" + queryparam + "%", "%" + queryparam + "%", c).size())
        InvestmentArchives.listOrderByCustomer()
        return json

    }

    def findByParm(String queryparam) {
        if (null == queryparam) {
            queryparam = ""
        }
        def f = Fund.findAllByFundNameLike("%" + queryparam + "%")
//        InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrUsernameLike(f, "%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%")
        def ia = InvestmentArchives.findAllByFundInListOrMarkNumLikeOrContractNumLikeOrUsernameLike(f, "%" + queryparam + "%", "%" + queryparam + "%", "%" + queryparam + "%")
        return ia
    }

    //根据付息起始时间、期限、付息方式，生成对应多个付息时间
   static def scfxsj(Date startTime, String qx, String fxfs) {
        List list = new ArrayList()
        //日历对象
        Calendar calendar = Calendar.getInstance();
        //设置当前日期
        calendar.setTime(startTime);
        //获取期限的数值
        Double t = Double.valueOf(qx.substring(0, qx.length() - 1))
        if (fxfs == "N") {
            if (qx.contains("天")) {
                calendar.add(Calendar.DATE, (int) t)
            } else if (qx.contains("年")) {
                calendar.add(Calendar.MONTH, (int) (t * 12))
            }
            list.add(calendar.getTime())
        } else if (fxfs == "J") {
            (1..(int) (t * 4)).each {
                calendar.add(Calendar.MONTH, 3)
                list.add(calendar.getTime())
            }
        } else if (fxfs == "B") {
            (1..(int) (t * 2)).each {
                calendar.add(Calendar.MONTH, 6)
                list.add(calendar.getTime())
            }
        } else if (fxfs == "Y") {
            (1..(int) t).each {
                calendar.add(Calendar.YEAR, 1)
                list.add(calendar.getTime())
            }
        } else if (fxfs == "M") {
            (1..(int) t * 12).each {
                calendar.add(Calendar.MONTH, 1)
                list.add(calendar.getTime())
            }
        }else if(fxfs=="D"){
            list.add(new Date())
        }

        return list
    }
    /**
     * 返回每次付息的金额
     * @param ia
     * @return
     */
    def getPayOnceAmount(InvestmentArchives ia) {
        if (ia.nhsyl == 0 || ia.sjtzje == 0 || ia.payTimes.size() == 0) {
            return 0
        }
        return (ia.nhsyl * ia.sjtzje) / ia.payTimes.size()
    }

    /**
     * 获取投资档案的管理提成时间表(提成人,时间,金额)
     * @param ia
     * @return 一个数组
     */
    def getGltcList(InvestmentArchives ia) {
        def list = []
        def nowt = new Date();
        ia.gltcs.sort {
            a, b -> a.tcffsj < b.tcffsj
        }.each {
            Date tcffsj = it.tcffsj
            double amount = 0
            if (it.tcffsj > nowt) {
                tcffsj = it.tcffsj
                amount = it.tcje * 0.2
            }
            if (it.glffsj2 > nowt) {
                tcffsj = it.glffsj2
                amount = it.tcje * 0.7
            }
            if (it.glffsj3 > nowt) {
                tcffsj = it.glffsj3
                amount = it.tcje * 0.1
            }
            list.add([user: it.user, time: tcffsj, amount: amount])
        }
        return list
    }
    /**
     * 获取可用于创建新档案的合同编号 (已经登记过,并且没有使用的合同编号)
     * 过滤条件
     */
    def getVisibleContractNumber(String params) {

        def list = []
        if (params.length() < 5) {
            return list;
        }
        def contract = Contract.findAllByHtbh("%" + params + "%")
        def userContracts = InvestmentArchives.list()


    }

    /**
     * 检测合同编号是否可用
     * @param contractNum
     * @return
     */
    def IVisible(def contractNum) {

        if (contractNum == null || contractNum == "") {

            return new Exception("合同编号格式不正确")
        }

        if (InvestmentArchives.findByContractNum(contractNum) != null) {
            return new Exception("合同编号已经使用过了!")
        }
        def c = Contract.findByHtbh(contractNum)

        if (c == null) {
            return new Exception("合同编号未登记,请先进行合同登记!")
        }
        return null
    }
}
