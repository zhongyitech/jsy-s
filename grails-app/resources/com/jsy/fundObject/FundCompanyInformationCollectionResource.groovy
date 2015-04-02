package com.jsy.fundObject

import Models.MsgModel
import com.jsy.project.TSProject
import com.jsy.system.TypeConfig
import com.jsy.utility.DomainHelper
import com.jsy.utility.JsonResult
import com.jsy.utility.MyResponse
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

import javax.ws.rs.DELETE
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam


import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.POST
import javax.ws.rs.core.Response
import static com.jsy.utility.MyResponse.*

@Path('/api/fundCompanyInformation')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class FundCompanyInformationCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def fundCompanyInformationResourceService

    @POST
    Response create(FundCompanyInformation dto) {
        MyResponse.ok{
            fundCompanyInformationResourceService.create(dto)
        }
    }

    @PUT
    Response update(FundCompanyInformation dto, @QueryParam('id') Long id) {

        ok {
            dto.id = id;
            return fundCompanyInformationResourceService.update(dto)
        }
    }

    @DELETE
    @Path('/delete')
    Response delete(@QueryParam('id') Long id) {
        ok {
            return fundCompanyInformationResourceService.delete(id)
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(FundCompanyInformation, arg)

            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
        }
//            json = fundCompanyInformationResourceService.readAllForPage(arg.pagesize, arg.startposition, arg.keyword)
//            total = json.get("size")
//            fp = json.get("page")
    }


    @GET
    @Path("/readAll")
    Response readAll() {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def ja = new JSONArray()
        try {
            def page = fundCompanyInformationResourceService.readAll()
            page.each {
                JSONObject jsonObject = new JSONObject((it as JSON).toString());
                def pars = new JSONArray()
                it?.hhrpx?.split(",").each { fid ->
                    pars.put(FundCompanyInformation.get(Long.valueOf(fid)) as JSON)
                }
                jsonObject.put("partner", pars)
                ja.put(jsonObject)
            }

            result.put("rest_status", restStatus)
            result.put("rest_result", ja.toString())
            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
        } catch (Exception e) {
            restStatus = REST_STATUS_FAI;
            e.printStackTrace()
            result.put("rest_status", restStatus)
            result.put("rest_result", ja.toString())
            return Response.ok(result.toString()).status(500).build()
        }
    }

    @GET
    @Path('/listAll')
    Response listAll() {
        ok { return fundCompanyInformationResourceService.readAll() }
    }


    @GET
    @Path('/getRelateFund')
    Response getRelateFund(@QueryParam('companyid') Long companyid, @QueryParam('projectid') Long projectid) {
        ok {
            def rtn = [:]
            def fundCompanyInformation = FundCompanyInformation.get(companyid)
            rtn.banks = fundCompanyInformation.funds.collect { fund ->
                if (!fund.project) {
                    def rtn2 = [:]
                    rtn2.id = fund.id
                    rtn2.fundName = fund.fundName
                    rtn2
                }else{
                    null
                }
            }
            return rtn.banks = rtn.banks.unique()
        }
    }

    @GET
    @Path('/findByFund')
    Response getByFund(@QueryParam('id') Long id) {
        ok
                {
                    def rtn = [:]
                    Fund fund = Fund.get(id)
                    if (!fund) {
                        throw new Exception("id no found!")
                    }
                    def projects = TSProject.findAllByFund(fund)

                    def project_banks = projects.collect { project ->
                        project.fund.funcCompany?.bankAccount?.collect { bank ->
                            def rtn2 = [:]
                            rtn2.id = bank.id
                            rtn2.bankName = bank.bankName
                            rtn2.bankOfDeposit = bank.bankOfDeposit
                            rtn2.accountName = bank.accountName
                            rtn2.account = bank.account
                            rtn2.defaultAccount = bank.defaultAccount
                            rtn2.purposeName = bank.purposeName
                            rtn2

                        }
                    }
                    def banks = []
                    project_banks?.each {
                        if (it) {
                            banks.addAll(it)
                        }
                    }
                    rtn.banks = banks.unique();
                    rtn.projects = projects.collect { project ->
                        project.getProjectSimpleInfo()
                    }
                    return rtn
                }
    }


    @GET
    @Path("/findById")
    Response findById(@QueryParam('id') Long id) {
        ok {
            return FundCompanyInformation.get(id)
        }
    }

    //获取募集基金账户信息
    @GET
    @Path('/getAccount')
    Response getAccount(@QueryParam('id') Long id) {

        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def fc
        try {
//            fc = FundCompanyInformation.get(id).bankAccount
            fc = FundCompanyInformation.findByFundInList(Fund.get(id)).bankAccount
        } catch (Exception e) {
            restStatus = REST_STATUS_FAI
            e.printStackTrace()
            print(e)
        }

        result.put("rest_status", restStatus)
        result.put("rest_result", fc as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()

    }


    @GET
    @Path('/findByType')
    Response findByType(@QueryParam('type') Long type) {

        ok { return FundCompanyInformation.findAllByCompanyType(TypeConfig.get(type)) }
    }

    @GET
    @Path('/getAccountFromFundName')
    Response getAccountFromFundName(@QueryParam('fundName') String fundName) {
        ok { FundCompanyInformation.findAllByFund(Fund.findByFundName(fundName)).bankAccount }
    }

    /**
     * 获取有限合伙的公司列表,用于有限合伙选择列表
     * 类型为有限合伙的公司(企业)
     * @return
     */
    @GET
    @Path('/listForAddFund')
    Response listForAddFund() {
        def type=TypeConfig.findByTypeAndMapValue(6, 1)
        ok {
            def list = []
            FundCompanyInformation.findAllByCompanyType(type).collect {
                list.add([id: it.id, companyName: it.companyName, companyType: it.companyType.mapName])
            }
            return list
        }
    }
}
