package com.jsy.archives

import com.jsy.system.ToDoTask
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.provider.DomainObjectNotFoundException

class PaymentInfoResourceService {
    def springSecurityService

    def create(PaymentInfo dto) {
        dto.save()
    }

    def readAllForPage(Long pagesize, Long startposition, String queryparam) {
        JSONObject json = new JSONObject()
//        参数：pagesize 每页数据条数
//              startposition,查询起始位置
//        def user = User.findAllByChinaNameLike(queryparam)
//        def dp = Department.findAllByDeptNameLike("%"+queryparam+"%")
//        def user = User.findAllByChainNameLike("%"+queryparam+"%")
//        def fund = Fund.findAllByFundNameLike("%"+queryparam+"%")
//        print("dp.size="+dp.size())
//        print("user.size="+user.size())
//        print("fund.size="+fund.size())
        def pi = PaymentInfo.findAllByFundNameLikeAndIsAllow("%" + queryparam + "%", false)

//          未完成
//        JSONObject total_table = new JSONObject()
//        int j = 0;
//        for(int i = 0; i<pi.size();i++){
//            def fname = pi[i].fundName;
//            def tzje = pi[i].tzje;
//            int size = 1
//            int bj = pi[i].yfbj
//            int lx = pi[i].yflx
//            JSONObject total_item = total_table.get(fname);
//
//            for(def v  : total_item.values()){
//
//            }
//
//            if(total_item == null){
//                j++;
//                int k = j + 0;
//                JSONObject total_pi = new JSONObject()
//                total_pi.put('name', fname)
//                total_pi.put('tzje', tzje);
//                total_pi.put('size', size);
//                total_pi.put('bj', bj);
//                total_pi.put('lx', lx)
//
//;               total_table.put(k,total_pi);
//            }else{
//                JSONObject total_pi = total_table.get(fname)
//                def tzje_x = total_pi.get('tzje');
//                def size_x = total_pi.get('size')
//                def bj_x = total_pi.get('bj')
//                def lx_x = total_pi.get('lx')
//                tzje_x= tzje_x + tzje;
//                size_x++
//                bj_x = bj_x + bj
//                lx_x = lx_x + lx
//                total_item.put('tzje', tzje_x);
//                total_pi.put('size', size_x);
//                total_pi.put('bj', bj_x);
//                total_pi.put('lx', lx_x)
//                total_table.put(k,total_pi);
//            }
//
//        }
//
//
//
//
//        int ttIndex = 0
//        for (int i = 0; i<pi.size();i++){
//            JSONObject total_pi = new JSONObject()
//            int total_size = 0
//            int total_tzje = 0
//            int total_bj = 0
//            int total_lx = 0
//            String total_name = ""
//            print("i = "+i)
//            if (i == 0){
//                total_name = pi[i].fundName
//                total_size++
//                total_tzje = pi[i].tzje + total_tzje
//                total_bj = pi[i].yfbj + total_bj
//                total_lx = pi[i].yflx + total_lx
//            }
//            if (i != 0 && i != pi.size()-1){
//                if (pi[i].fundName.equals(total_name)){
//                    print("i.name=last.name")
//                    total_size++
//                    total_tzje = pi[i].tzje + total_tzje
//                    total_bj = pi[i].yfbj + total_bj
//                    total_lx = pi[i].yflx + total_lx
//                }else {
//                    print("i.name NOT EQUALS last.name")
//                    ttIndex++
////                    String name = new String(total_name.toString())
////                    String total = new String(total_size.toString())
////                    String tzje = new String(total_tzje.toString())
////                    String bj = new String(total_bj.toString())
////                    String lx = new String(total_lx.toString())
//                    total_pi.put("total_name", total_name)
//                    total_pi.put("total_total",total_size)
//                    total_pi.put("total_tzje", total_tzje)
//                    total_pi.put("total_bj", total_bj)
//                    total_pi.put("total_lx", total_lx)
//                    print("Index = "+ttIndex+" current table = "+ total_pi)
//                    total_table.put(ttIndex.toString(), total_pi)
//                    print("Index = "+ttIndex+" total_table.currrent = "+total_table.get(ttIndex.toString()))
//                    print("Index = "+ttIndex+" get all table = "+total_table)
//                    total_tzje = 0
//                    total_bj = 0
//                    total_lx = 0
//                    total_size = 1
//                    total_name = pi[i].fundName
//                    total_tzje = pi[i].tzje + total_tzje
//                    total_bj = pi[i].yfbj + total_bj
//                    total_lx = pi[i].yflx + total_lx
//                }
//
//            }
//            if (i == pi.size()-1){
//                print("i = the last row")
//                if (pi[i].fundName.equals(total_name)){
//                    print("i.name=last.name")
//                    total_size++
//                    total_tzje = pi[i].tzje + total_tzje
//                    total_bj = pi[i].yfbj + total_bj
//                    total_lx = pi[i].yflx + total_lx
//                }else {
//                    print("i.name NOT EQUALS last.name")
//                    ttIndex++
////                    String name = new String(total_name.toString())
////                    String total = new String(total_size.toString())
////                    String tzje = new String(total_tzje.toString())
////                    String bj = new String(total_bj.toString())
////                    String lx = new String(total_lx.toString())
//                    total_pi.put("total_name", total_name)
//                    total_pi.put("total_total",total_size)
//                    total_pi.put("total_tzje", total_tzje)
//                    total_pi.put("total_bj", total_bj)
//                    total_pi.put("total_lx", total_lx)
//                    print("Index = "+ttIndex+" current table = "+ total_pi)
//                    total_table.put(ttIndex.toString(), total_pi)
//                    print("Index = "+ttIndex+" total_table.currrent = "+total_table.get(ttIndex.toString()))
//                    print("Index = "+ttIndex+" get all table = "+total_table)
//                    total_tzje = 0
//                    total_bj = 0
//                    total_lx = 0
//                    total_size = 1
//                    total_name = pi[i].fundName
//                    total_tzje = pi[i].tzje + total_tzje
//                    total_bj = pi[i].yfbj + total_bj
//                    total_lx = pi[i].yflx + total_lx
//                }
//                ttIndex++
////                String name = new String(total_name.toString())
////                String total = new String(total_size.toString())
////                String tzje = new String(total_tzje.toString())
////                String bj = new String(total_bj.toString())
////                String lx = new String(total_lx.toString())
//                total_pi.put("total_name", total_name)
//                total_pi.put("total_total",total_size)
//                total_pi.put("total_tzje", total_tzje)
//                total_pi.put("total_bj", total_bj)
//                total_pi.put("total_lx", total_lx)
//                print("Index = "+ttIndex+" current table = "+ total_pi)
//                total_table.put(ttIndex.toString(), total_pi)
//                print("Index = "+ttIndex+" total_table.currrent = "+total_table.get(ttIndex.toString()))
//                print("Index = "+ttIndex+" get all table = "+total_table)
//                total_tzje = 0
//                total_bj = 0
//                total_lx = 0
//                total_size = 1
//                total_name = pi[i].fundName
//                total_tzje = pi[i].tzje + total_tzje
//                total_bj = pi[i].yfbj + total_bj
//                total_lx = pi[i].yflx + total_lx
//            }
//            print("ttIndex = " + ttIndex)
//            print("total.name = "+total_name)
//            print("total.size = "+total_size)
//            print("total.tzje = "+total_tzje)
//            print("total.bj = "+total_bj)
//            print("total.lx = "+total_lx)
//        }
//        pi.each {
//            if (0 == ttIndex){
//
//            }else {
//                if (it.fundName.equals(total_name)){
//                    total_size++
//                    total_tzje = it.tzje + total_tzje
//                    total_bj = it.yfbj + total_bj
//                    total_lx = it.yflx + total_lx
//                }else {
//                    ttIndex++
//                    total_pi.put("total_name", total_name)
//                    total_pi.put("total_total",total_size)
//                    total_pi.put("total_tzje", total_tzje)
//                    total_pi.put("total_bj", total_bj)
//                    total_pi.put("total_lx", total_lx)
//                    total_table.put(ttIndex.toString(), total_pi)
//                    total_tzje = 0
//                    total_bj = 0
//                    total_lx = 0
//                    total_size = 1
//                    total_name = it.fundName
//                    total_tzje = it.tzje + total_tzje
//                    total_bj = it.yfbj + total_bj
//                    total_lx = it.yflx + total_lx
//                }
//            }
//
//        }

////        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundNameInList(user, dp, fund, [max: pagesize, offset: startposition])
//        RegisterContract.findAllByReceiveUserInListOrDepartmentInListOrFundInList(user, dp, fund, [max: pagesize, offset: startposition])
//        InvestmentArchives.findAllByFundNameLikeOrMarkNumLikeOrContractNumLike("%"+queryparam+"%", "%"+queryparam+"%", "%"+queryparam+"%", [max: pagesize, offset: startposition])
        json.put("page", PaymentInfo.findAllByFundNameLikeAndIsAllow("%" + queryparam + "%", false, [max: pagesize, offset: startposition]))
        json.put("size", PaymentInfo.findAllByFundNameLikeAndIsAllow("%" + queryparam + "%", false).size())
//        json.put("total_table", total_table)

        return json

    }

    def read(id) {
        def obj = PaymentInfo.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PaymentInfo.class, id)
        }
        obj
    }

    def readAll() {
        PaymentInfo.findAllByIsAllow(false)
    }

    def update(PaymentInfo dto) {
        def obj = PaymentInfo.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(PaymentInfo.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = PaymentInfo.get(id)
        if (obj) {
            obj.delete()
        }
    }

    //新的生成兑付申请
    def addPaymentInfo() {
        List<InvestmentArchives> investmentArchives = InvestmentArchives.findAllByStopPay(false)
        investmentArchives.each {
            try {
                int t = PaymentInfo.findAllByArchivesId(it.id).size()
                //todo: status==1 正常(已入库)
                if ( it.customer != null && it.status == 1 && it.payTimes.size() > t) {
                    it.payTimes.each { p ->
                        if (p.px == t + 1 && p.sffx == false) {
                            if (p.fxsj.before(new Date())) {
                                //根据档案的客户分配，新增一条记录
                                PaymentInfo paymentInfo = new PaymentInfo()
                                paymentInfo.archivesId = it.id
                                paymentInfo.fxsj = p.fxsj
                                paymentInfo.fundName = it.fund.fundName
                                paymentInfo.htbh = it.archiveNum
                                paymentInfo.customerName = it.customer.name
                                paymentInfo.tzje = it.tzje
                                paymentInfo.tzqx = it.tzqx
                                paymentInfo.syl = it.nhsyl
//                                print(it.bmjl?.properties)
                                paymentInfo.bmjl = it.bmjl?.chainName
                                paymentInfo.khh = it.customer.khh
                                paymentInfo.yhzh = it.customer.yhzh
                                paymentInfo.gj = it.customer.country
                                paymentInfo.zjlx = it.customer.credentialsType
                                paymentInfo.zjhm = it.customer.credentialsNumber
                                //计算应付利息
                                BigDecimal yflx = (it.tzje * it.nhsyl) / it.payTimes.size()
                                paymentInfo.yflx = yflx
                                //计算应付本金
                                BigDecimal yfbj = 0
                                if (t == p.px + 1) {
                                    yfbj = it.bj
                                }
                                paymentInfo.yfbj = yfbj
                                paymentInfo.zj = yflx + yfbj
                                paymentInfo.save(failOnError: true)
                                //将这次付息改为已付
                                p.sffx = true
                                p.save(failOnError: true)
                                if (t == p.px + 1) {
                                    //将档案表的付款状态改为付款完成
                                    it.stopPay = true
                                    it.save(failOnError: true)
                                }
                                print("save ok!")
                            }
                        }
                    }
                }
                else {
                }
            }catch (Exception ex){
                print(ex)
            }
        }
    }

    //检测投资档案是否生成兑换申请
//    def addPaymentInfo(){
//        List<InvestmentArchives> investmentArchives=InvestmentArchives.findAllByStopPay(false)
//        investmentArchives.each {
//            int i=PaymentInfo.findAllByArchivesId(it.id).size()
//            if(it.customerCommision.size()==0&&it.customer!=null&&it.status==1){
//                if (i == 0) {
//                    if (it.fxsj1 && it.fxsj1.before(new Date())) {
//                        //根据档案的客户分配
//                            //新增一条记录
//                            PaymentInfo paymentInfo = new PaymentInfo()
//                            paymentInfo.archivesId = it.id
//                            paymentInfo.fxsj = it.fxsj1
//                            paymentInfo.fundName = it.fund.fundName
//                            paymentInfo.htbh = it.archiveNum
//                            paymentInfo.customerName = it.customer.name
//                            paymentInfo.tzje = it.tzje
//                            paymentInfo.tzqx = it.tzqx
//                            paymentInfo.syl = it.nhsyl
//                            paymentInfo.bmjl = it.bmjl.chainName
//                            if (it.fxfs == "N") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl
//                                //计算应付本金
//                                BigDecimal yfbj = it.bj
//                                paymentInfo.khh = it.customer.khh
//                                paymentInfo.yhzh = it.customer.yhzh
//                                paymentInfo.gj = it.customer.country
//                                paymentInfo.zjlx = it.customer.credentialsType
//                                paymentInfo.zjhm = it.customer.credentialsNumber
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                                //将档案表的付款状态改为付款完成
//                                it.stopPay = true
//                                it.save(failOnError: true)
//                            } else if (it.fxfs == "W") {
//                                //计算应付利息
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl*0.5
//                                //计算应付本金
//                                BigDecimal yfbj = 0
//                                paymentInfo.khh = it.customer.khh
//                                paymentInfo.yhzh = it.customer.yhzh
//                                paymentInfo.gj = it.customer.country
//                                paymentInfo.zjlx = it.customer.credentialsType
//                                paymentInfo.zjhm = it.customer.credentialsNumber
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            } else if (it.fxfs == "J") {
//                                //计算应付利息
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl*0.25
//                                //计算应付本金
//                                BigDecimal yfbj = 0
//                                paymentInfo.khh = it.customer.khh
//                                paymentInfo.yhzh = it.customer.yhzh
//                                paymentInfo.gj = it.customer.country
//                                paymentInfo.zjlx = it.customer.credentialsType
//                                paymentInfo.zjhm = it.customer.credentialsNumber
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            }
//                    }
//                } else if (i == 1) {
//                    if (it.fxsj2 && it.fxsj2.before(new Date())) {
//                        //根据档案的客户分配
//                            //新增一条记录
//                            PaymentInfo paymentInfo = new PaymentInfo()
//                            paymentInfo.archivesId = it.id
//                            paymentInfo.fxsj = it.fxsj1
//                            paymentInfo.fundName = it.fund.fundName
//                            paymentInfo.htbh = it.archiveNum
//                            paymentInfo.customerName = it.customer.name
//                            paymentInfo.tzje = it.tzje
//                            paymentInfo.tzqx = it.tzqx
//                            paymentInfo.syl = it.nhsyl
//                            paymentInfo.bmjl = it.bmjl.chainName
//                            if (it.fxfs == "W") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl*0.5
//                                //计算应付本金
//                                BigDecimal yfbj = it.bj
//                                paymentInfo.khh = it.customer.khh
//                                paymentInfo.yhzh = it.customer.yhzh
//                                paymentInfo.gj = it.customer.country
//                                paymentInfo.zjlx = it.customer.credentialsType
//                                paymentInfo.zjhm = it.customer.credentialsNumber
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                                //将档案表的付款状态改为付款完成
//                                it.stopPay = true
//                                it.save(failOnError: true)
//                            } else if (it.fxfs == "J") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl*0.25
//                                //计算应付本金
//                                BigDecimal yfbj = 0
//                                paymentInfo.khh = it.customer.khh
//                                paymentInfo.yhzh = it.customer.yhzh
//                                paymentInfo.gj = it.customer.country
//                                paymentInfo.zjlx = it.customer.credentialsType
//                                paymentInfo.zjhm = it.customer.credentialsNumber
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            }
//                    }
//                } else if (i == 2) {
//                    if (it.fxsj3 && it.fxsj3.before(new Date())) {
//                        //根据档案的客户分配
//                            //新增一条记录
//                            PaymentInfo paymentInfo = new PaymentInfo()
//                            paymentInfo.archivesId = it.id
//                            paymentInfo.fxsj = it.fxsj1
//                            paymentInfo.fundName = it.fund.fundName
//                            paymentInfo.htbh = it.archiveNum
//                            paymentInfo.customerName = it.customer.name
//                            paymentInfo.tzje = it.tzje
//                            paymentInfo.tzqx = it.tzqx
//                            paymentInfo.syl = it.nhsyl
//                            paymentInfo.bmjl = it.bmjl.chainName
//                            if (it.fxfs == "J") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl*0.25
//                                //计算应付本金
//                                BigDecimal yfbj = 0
//                                paymentInfo.khh = it.customer.khh
//                                paymentInfo.yhzh = it.customer.yhzh
//                                paymentInfo.gj = it.customer.country
//                                paymentInfo.zjlx = it.customer.credentialsType
//                                paymentInfo.zjhm = it.customer.credentialsNumber
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            }
//                    }
//                } else if (i == 3) {
//                    if (it.fxsj4 && it.fxsj4.before(new Date())) {
//                        if (it.fxsj3 && it.fxsj3.before(new Date())) {
//                            //根据档案的客户分配
//                            it.customerCommision.each { cc ->
//                                //新增一条记录
//                                PaymentInfo paymentInfo = new PaymentInfo()
//                                paymentInfo.archivesId = it.id
//                                paymentInfo.fxsj = it.fxsj1
//                                paymentInfo.fundName = it.fund.fundName
//                                paymentInfo.htbh = it.archiveNum
//                                paymentInfo.customerName = it.customer.name
//                                paymentInfo.tzje = it.tzje
//                                paymentInfo.tzqx = it.tzqx
//                                paymentInfo.syl = it.nhsyl
//                                paymentInfo.bmjl = it.bmjl.chainName
//                                if (it.fxfs == "J") {
//                                    //计算应付利息
//                                    BigDecimal yflx = it.tzje * it.nhsyl*0.25
//                                    //计算应付本金
//                                    BigDecimal yfbj = it.bj
//                                    paymentInfo.khh = it.customer.khh
//                                    paymentInfo.yhzh = it.customer.yhzh
//                                    paymentInfo.gj = it.customer.country
//                                    paymentInfo.zjlx = it.customer.credentialsType
//                                    paymentInfo.zjhm = it.customer.credentialsNumber
//                                    paymentInfo.yflx = yflx
//                                    paymentInfo.yfbj = yfbj
//                                    paymentInfo.zj = yflx + yfbj
//                                    paymentInfo.save(failOnError: true)
//                                    //将档案表的付款状态改为付款完成
//                                    it.stopPay = true
//                                    it.save(failOnError: true)
//                                }
//                            }
//                        }
//                    }
//                }
//            }else {
//                if (i == 0) {
//                    if (it.fxsj1 && it.fxsj1.before(new Date())) {
//                        //根据档案的客户分配
//                        it.customerCommision.each { cc ->
//                            //新增一条记录
//                            PaymentInfo paymentInfo = new PaymentInfo()
//                            paymentInfo.archivesId = it.id
//                            paymentInfo.fxsj = it.fxsj1
//                            paymentInfo.fundName = it.fund.fundName
//                            paymentInfo.htbh = it.archiveNum
//                            paymentInfo.customerName = it.customer.name
//                            paymentInfo.tzje = it.tzje
//                            paymentInfo.tzqx = it.tzqx
//                            paymentInfo.syl = it.nhsyl
//                            paymentInfo.bmjl = it.bmjl.chainName
//                            if (it.fxfs == "N") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl
//                                //计算应付本金
//                                BigDecimal yfbj = it.tzje * cc.bjbl
//                                paymentInfo.khh = cc.khh
//                                paymentInfo.yhzh = cc.yhzh
//                                paymentInfo.gj = cc.gj
//                                paymentInfo.zjlx = cc.zjlx
//                                paymentInfo.zjhm = cc.zjhm
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                                //将档案表的付款状态改为付款完成
//                                it.stopPay = true
//                                it.save(failOnError: true)
//                            } else if (it.fxfs == "W") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl * 0.5
//                                //计算应付本金
//                                BigDecimal yfbj = it.tzje * cc.bjbl * 0.5
//                                paymentInfo.khh = cc.khh
//                                paymentInfo.yhzh = cc.yhzh
//                                paymentInfo.gj = cc.gj
//                                paymentInfo.zjlx = cc.zjlx
//                                paymentInfo.zjhm = cc.zjhm
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            } else if (it.fxfs == "J") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl * 0.25
//                                //计算应付本金
//                                BigDecimal yfbj = it.tzje * cc.bjbl * 0.25
//                                paymentInfo.khh = cc.khh
//                                paymentInfo.yhzh = cc.yhzh
//                                paymentInfo.gj = cc.gj
//                                paymentInfo.zjlx = cc.zjlx
//                                paymentInfo.zjhm = cc.zjhm
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            }
//                        }
//                    }
//                } else if (i == 1) {
//                    if (it.fxsj2 && it.fxsj2.before(new Date())) {
//                        //根据档案的客户分配
//                        it.customerCommision.each { cc ->
//                            //新增一条记录
//                            PaymentInfo paymentInfo = new PaymentInfo()
//                            paymentInfo.archivesId = it.id
//                            paymentInfo.fxsj = it.fxsj1
//                            paymentInfo.fundName = it.fund.fundName
//                            paymentInfo.htbh = it.archiveNum
//                            paymentInfo.customerName = it.customer.name
//                            paymentInfo.tzje = it.tzje
//                            paymentInfo.tzqx = it.tzqx
//                            paymentInfo.syl = it.nhsyl
//                            paymentInfo.bmjl = it.bmjl.chainName
//                            if (it.fxfs == "W") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl * 0.5
//                                //计算应付本金
//                                BigDecimal yfbj = it.tzje * cc.bjbl * 0.5
//                                paymentInfo.khh = cc.khh
//                                paymentInfo.yhzh = cc.yhzh
//                                paymentInfo.gj = cc.gj
//                                paymentInfo.zjlx = cc.zjlx
//                                paymentInfo.zjhm = cc.zjhm
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                                //将档案表的付款状态改为付款完成
//                                it.stopPay = true
//                                it.save(failOnError: true)
//                            } else if (it.fxfs == "J") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl * 0.25
//                                //计算应付本金
//                                BigDecimal yfbj = it.tzje * cc.bjbl * 0.25
//                                paymentInfo.khh = cc.khh
//                                paymentInfo.yhzh = cc.yhzh
//                                paymentInfo.gj = cc.gj
//                                paymentInfo.zjlx = cc.zjlx
//                                paymentInfo.zjhm = cc.zjhm
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            }
//                        }
//                    }
//                } else if (i == 2) {
//                    if (it.fxsj3 && it.fxsj3.before(new Date())) {
//                        //根据档案的客户分配
//                        it.customerCommision.each { cc ->
//                            //新增一条记录
//                            PaymentInfo paymentInfo = new PaymentInfo()
//                            paymentInfo.archivesId = it.id
//                            paymentInfo.fxsj = it.fxsj1
//                            paymentInfo.fundName = it.fund.fundName
//                            paymentInfo.htbh = it.archiveNum
//                            paymentInfo.customerName = it.customer.name
//                            paymentInfo.tzje = it.tzje
//                            paymentInfo.tzqx = it.tzqx
//                            paymentInfo.syl = it.nhsyl
//                            paymentInfo.bmjl = it.bmjl.chainName
//                            if (it.fxfs == "J") {
//                                //计算应付利息
//                                BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl * 0.25
//                                //计算应付本金
//                                BigDecimal yfbj = it.tzje * cc.bjbl * 0.25
//                                paymentInfo.khh = cc.khh
//                                paymentInfo.yhzh = cc.yhzh
//                                paymentInfo.gj = cc.gj
//                                paymentInfo.zjlx = cc.zjlx
//                                paymentInfo.zjhm = cc.zjhm
//                                paymentInfo.yflx = yflx
//                                paymentInfo.yfbj = yfbj
//                                paymentInfo.zj = yflx + yfbj
//                                paymentInfo.save(failOnError: true)
//                            }
//                        }
//                    }
//                } else if (i == 3) {
//                    if (it.fxsj4 && it.fxsj4.before(new Date())) {
//                        if (it.fxsj3 && it.fxsj3.before(new Date())) {
//                            //根据档案的客户分配
//                            it.customerCommision.each { cc ->
//                                //新增一条记录
//                                PaymentInfo paymentInfo = new PaymentInfo()
//                                paymentInfo.archivesId = it.id
//                                paymentInfo.fxsj = it.fxsj1
//                                paymentInfo.fundName = it.fund.fundName
//                                paymentInfo.htbh = it.archiveNum
//                                paymentInfo.customerName = it.customer.name
//                                paymentInfo.tzje = it.tzje
//                                paymentInfo.tzqx = it.tzqx
//                                paymentInfo.syl = it.nhsyl
//                                paymentInfo.bmjl = it.bmjl.chainName
//                                if (it.fxfs == "J") {
//                                    //计算应付利息
//                                    BigDecimal yflx = it.tzje * it.nhsyl * cc.lxbl * 0.25
//                                    //计算应付本金
//                                    BigDecimal yfbj = it.tzje * cc.bjbl * 0.25
//                                    paymentInfo.khh = cc.khh
//                                    paymentInfo.yhzh = cc.yhzh
//                                    paymentInfo.gj = cc.gj
//                                    paymentInfo.zjlx = cc.zjlx
//                                    paymentInfo.zjhm = cc.zjhm
//                                    paymentInfo.yflx = yflx
//                                    paymentInfo.yfbj = yfbj
//                                    paymentInfo.zj = yflx + yfbj
//                                    paymentInfo.save(failOnError: true)
//                                    //将档案表的付款状态改为付款完成
//                                    it.stopPay = true
//                                    it.save(failOnError: true)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    //同意生成兑付申请
    def toPay(Long payId) throws Exception {
        PaymentInfo paymentInfo = PaymentInfo.get(payId)
        if (paymentInfo.type != 0) {
            return new Exception('正在处理或已处理！')
        }
        //提交到OA不能修改了
        paymentInfo.type = 1
        //OA通过了
        if (paymentInfo.yflx != 0) {
            new Payment(infoId: paymentInfo.id, zfsj: paymentInfo.zfsj, fpe: paymentInfo.yflx, fundName: paymentInfo.fundName, contractNum: paymentInfo.htbh, customerName: paymentInfo.customerName, khh: paymentInfo.khh, zh: paymentInfo.yhzh, bmjl: paymentInfo.bmjl, yfk: paymentInfo.yflx, dflx: "lx").save(failOnError: true)
        }
        if (paymentInfo.yfbj != 0) {
            new Payment(infoId: paymentInfo.id, zfsj: paymentInfo.zfsj, fpe: paymentInfo.yfbj, fundName: paymentInfo.fundName, contractNum: paymentInfo.htbh, customerName: paymentInfo.customerName, khh: paymentInfo.khh, zh: paymentInfo.yhzh, bmjl: paymentInfo.bmjl, yfk: paymentInfo.yfbj, dflx: "bj").save(failOnError: true)
        }
        paymentInfo.type = 2
        paymentInfo.isAllow = true
        paymentInfo.save(failOnError: true)
        //修改待办任务
        ToDoTask toDoTask = ToDoTask.get(paymentInfo.todoId)
        toDoTask.clr = springSecurityService.getCurrentUser()
        toDoTask.clsj = new Date()
        toDoTask.status = 1
    }

    /**
     * 获取已付利息和已付本金的操作
     * @param investment
     * @return
     */
    def getPaymentAmount(Long investment) {
        def bj = 0
        def lx = 0
        PaymentInfo.findAllByArchivesIdAndIsAllowAndType(investment, true, 2)
                .each {
            bj += it.getYfbj()
            lx += it.getYflx()
        }
        return [bj: bj, lx: lx]
    }
}
