package com.jsy.utility

import com.jsy.archives.InvestmentArchives
import com.jsy.archives.InvestmentArchivesResourceService
import com.jsy.archives.PayTime
import com.jsy.archives.UserCommision
import com.jsy.fundObject.Fund
import grails.transaction.Transactional

@Transactional(rollbackFor = Throwable.class)
class YieldService {

    /**
     * 获取年化收益率及提成比例信息
     * @param fundid
     * @param manageid
     * @param investment
     * @param vers
     * @return
     */
    public static def getYield(Long fundid, Long manageid, BigDecimal investment, String vers) {
        //vers指合同版本，根据不同版本，得到不同的收益率
        def obj = [:]
        Fund fund = Fund.get(fundid)
        fund.tcfpfw.each {
            if (it.manageerId == manageid) {
                obj.rest_tc = it
            }
        }
        obj.rest_yield = fund.getYieldRange(investment, vers)
        return obj
    }

    /**
     * 重新 投资档案收益率情况 根据投档档案的参数
     * @param ia
     * @return
     */
    public static def restGetYield(InvestmentArchives ia) {
        def ver = ia.contractNum.substring(3, 4)
        def yield = getYield(ia.fund.id, ia.bmjl.id, ia.tzje, ver.toUpperCase())
        ia.nhsyl = yield.rest_yield
    }

    /**
     * 重新计算投资档案的提成数据
     * @param ia
     */
    public static def restSetTc(InvestmentArchives dto) {
        def yw = dto.ywtcs.toList()
        def gl = dto.gltcs.toList()
        dto.ywtcs = []
        dto.gltcs = []
        yw.each {
            UserCommision uc = new UserCommision()
            uc.properties = it.properties
            uc.id = null
            uc.glffsj3 = uc.glffsj2 = uc.sjffsj = null
            uc.tcffsj = DateUtility.lastDayWholePointDate(dto.rgrq)
            uc.tcje = dto.tzje * dto.ywtc * uc.tcbl
            dto.ywtcs.add(uc)
        }
        Calendar rightNow = Calendar.getInstance();
        def nowDt = DateUtility.lastDayWholePointDate(dto.rgrq)
        gl.each {
            //TODO:覆盖前台传递的管理提成发放时间  第一次70%:下一月5号 20%: 下一年末 10% 再下一年末
            rightNow.setTime(nowDt);
            rightNow.add(Calendar.MONTH, 1)
            rightNow.set(Calendar.DAY_OF_MONTH, 5)
            def gluc = new UserCommision()
            gluc.properties = it.properties
            gluc.id = null
            //下一个
            gluc.tcffsj = rightNow.getTime()
            //第二年年末
            gluc.glffsj2 = DateUtility.lastDayWholePointDate(DateUtility.getCurrYearLast(rightNow.get(Calendar.YEAR) + 1))
            //第三年年开
            gluc.glffsj3 = DateUtility.lastDayWholePointDate(DateUtility.getCurrYearLast(rightNow.get(Calendar.YEAR) + 2))
            gluc.sjffsj = null
            gluc.real_glffsj2 = null
            gluc.real_glffsj3 = null
            dto.gltcs.add(gluc)
        }
    }
    /**
     * 生成付息时间，务必在保存之后操作
     * @param dto
     * @return
     */
    def restPayTime(InvestmentArchives ia) {
        ia.payTimes = []
        List times = InvestmentArchivesResourceService.scfxsj(ia.rgrq, ia.tzqx, ia.fxfs)
        int i = 1
        //生成兑付记录
        times.each {
            PayTime payTime = new PayTime(px: i, fxsj: it, sffx: false, investmentArchives: ia)
            ia.payTimes.add(payTime)
            i++
        }
    }
}
