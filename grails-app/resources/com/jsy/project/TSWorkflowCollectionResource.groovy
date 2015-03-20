package com.jsy.project

import GsonTools.GsonTool
import Models.MsgModel
import com.jsy.archives.CustomerArchives
import com.jsy.archives.InvestmentArchives
import com.jsy.archives.PaymentInfo
import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Finfo
import com.jsy.fundObject.Fund
import com.jsy.system.TypeConfig
import grails.converters.JSON
import sun.misc.resources.Messages_sv

import javax.ws.rs.QueryParam

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/TSWorkflow')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class TSWorkflowCollectionResource {

    WorkflowResourceService workflowResourceService


    @GET
    @Path('/irData')
    Response initData() {
        workflowResourceService.initData();
        ok "good"
    }

    @GET
    @Path('/irData2')
    Response initData2() {
        Customer customer = new Customer(name:'liayi',credentialsAddr:'at sss',credentialsNumber:'1111');
        customer.save(failOnError: true)

        CustomerArchives customerArchive = new CustomerArchives(
                name:'1',
                country:'2',
                credentialsType:'3',
                credentialsNumber:'4',
                credentialsAddr:'5',
                fddbr:'6',
                zch:'7',
                khh:'8',
                telephone:'9',
                phone:'10',
                postalcode:'11',
                callAddress:'12');
        customerArchive.save(failOnError: true)

        def typeConfig =TypeConfig.findByMapName("在库")
        def fund = Fund.findByFundName('fund1')?:new Fund(fundName:'fund1',fundNo:'F001',startSaleDate:new Date(),status:TypeConfig.findByTypeAndMapValue(1,2),owner:'张三',memo:'备注').save(failOnError: true)
        def user = User.findByUsername("admin")

        InvestmentArchives archives1 = new InvestmentArchives(markNum:'2141412',customerArchive:customerArchive,customer:customer,
                archiveNum:1,contractNum:'124214',fund:fund,tzje:1,tzqx:'2015-06-01',rgrq:new Date(),dqrq:new Date(),fxfs:'fukuan',nhsyl:0.01,
                htzt:typeConfig,bm:'abc',gltc:0.1,ywtc:0.1,bj:10000,
        )
        archives1.save(failOnError: true)
        PaymentInfo paymentInfo = new PaymentInfo(fundName:'fund3',htbh:'3000',customerName:'sss',tzje:12,
                tzqx:'2015-05-14',syl:0.1,yflx:0.1,yfbj:100,
                zj:100,khh:'211111',yhzh:'cc',gj:'bbb',zjlx:'dd',zjhm:'ddd',bmjl:'ddd',
                archivesId:archives1.id,fxsj:new Date(),type:0)
        paymentInfo.save(failOnError: true)
        ok "good2"
    }

    @GET
    @Path('/setFundCompanyInformation')
    Response setFundCompanyInformation(){
        workflowResourceService.initFundCompanyInformation();

        ok "init FundCompanyInformation ok";
    }
}



























