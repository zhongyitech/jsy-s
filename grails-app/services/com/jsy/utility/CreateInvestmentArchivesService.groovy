package com.jsy.utility

import com.jsy.archives.InvestmentArchives
import com.jsy.archives.PaymentInfo
import com.jsy.archives.UserCommision
import com.jsy.fundObject.Fund
import grails.transaction.Transactional

import java.text.SimpleDateFormat

@Transactional
class CreateInvestmentArchivesService {

    def serviceMethod() {

    }
    //根据旧档案和新基金生成新的档案
    def create(Fund fund,InvestmentArchives oldInv,BigDecimal je,String htbh,Date kssj){
        InvestmentArchives newInv=new InvestmentArchives()
        newInv.properties=oldInv.properties
        newInv.archiveFrom=oldInv.id
        newInv.dazt=0
        newInv.status=1
        newInv.tzje=je
        newInv.dycs=0
        newInv.contractNum=htbh
        newInv.startDate=new Date()
        StringBuffer former = CreateNumberService.getFormerNumber(new StringBuffer("I"))
        newInv.archiveNum = CreateNumberService.getRandomNumber(new StringBuffer(former))
        newInv.markNum = newInv.archiveNum
        newInv.nhsyl=GetYieldService.getYield(fund.id, oldInv.bmjl.id, je,htbh.substring(3,4))//截取合同版本，生成收益率
        newInv.save(failOnError: true)
        //档案附件
        oldInv.uploadFiles.each {
            newInv.addToUploadFiles(it)
        }
        //根据付息方式生成发放时间
        if(newInv.fxfs=="N"){
            Long t=kssj.getTime()-oldInv.rgrq
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(oldInv.fxsj1.getTime()+t)
            newInv.fxsj1=c.getTime()
        }else if(newInv.fxfs=="W"){
            Long t=kssj.getTime()-oldInv.rgrq
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(oldInv.fxsj1.getTime()+t)
            newInv.fxsj1=c.getTime()
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(oldInv.fxsj2.getTime()+t)
            newInv.fxsj2=c1.getTime()
        }else if(newInv.fxfs=="j"){
            Long t=kssj.getTime()-oldInv.rgrq
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(oldInv.fxsj1.getTime()+t)
            newInv.fxsj1=c.getTime()
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(oldInv.fxsj2.getTime()+t)
            newInv.fxsj2=c1.getTime()
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(oldInv.fxsj3.getTime()+t)
            newInv.fxsj3=c2.getTime()
            Calendar c3 = Calendar.getInstance();
            c3.setTimeInMillis(oldInv.fxsj4.getTime()+t)
            newInv.fxsj4=c3.getTime()
        }else{
            print("no fxfs")
        }
        //循环重新生成提成
        oldInv.ywtcs.each {
            newInv.removeFromYwtcs(it)
            UserCommision ywtc=new UserCommision()
            ywtc.properties=it.properties
            ywtc.tcje=je*newInv .nhsyl*oldInv.ywtc*it.tcbl
            ywtc.save(failOnError: true)
            newInv.addToYwtcs(ywtc)
        }
        oldInv.gltcs.each {
            newInv.removeFromGltcs(it)
            UserCommision glct=new UserCommision()
            glct.properties=it.properties
            glct.tcje=je*newInv .nhsyl*oldInv.gltc*it.tcbl
            //更新发放时间
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            glct.tcffsj=cale.getTime()
            glct.glffsj2=getYearLast(cale.get(Calendar.YEAR))
            glct.glffsj3=getYearLast(cale.get(Calendar.YEAR)+1)
            glct.save(failOnError: true)
            newInv.addToYwtcs(glct)
        }
        return newInv.save(failOnError: true)
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    def Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    /**
     * 本金支付
     */
    def playBJ(InvestmentArchives investmentArchives){
        PaymentInfo paymentInfo = new PaymentInfo()
        paymentInfo.archivesId = investmentArchives.id
        paymentInfo.fxsj = investmentArchives.fxsj1
        paymentInfo.fundName = investmentArchives.fund.fundName
        paymentInfo.htbh = investmentArchives.archiveNum
        paymentInfo.customerName = investmentArchives.customer.name
        paymentInfo.tzje = investmentArchives.tzje
        paymentInfo.tzqx = investmentArchives.tzqx
        paymentInfo.syl = investmentArchives.nhsyl
        paymentInfo.bmjl = investmentArchives.bmjl.chainName
        //计算应付利息
        BigDecimal yflx = 0
        //计算应付本金
        BigDecimal yfbj = investmentArchives.bj
        paymentInfo.khh = investmentArchives.customer.khh
        paymentInfo.yhzh = investmentArchives.customer.yhzh
        paymentInfo.gj = investmentArchives.customer.country
        paymentInfo.zjlx = investmentArchives.customer.credentialsType
        paymentInfo.zjhm = investmentArchives.customer.credentialsNumber
        paymentInfo.yflx = yflx
        paymentInfo.yfbj = yfbj
        paymentInfo.zj = yflx + yfbj
        paymentInfo.save(failOnError: true)
        investmentArchives.bj=0
        investmentArchives.save(failOnError: true)
    }

}
