package com.jsy.archives

import com.jsy.auth.User
import com.jsy.utility.DomainHelper
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

import javax.ws.rs.DELETE
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/customerArchives')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class CustomerArchivesCollectionResource {
    CustomerArchivesResourceService customerArchivesResourceService

    @PUT
    Response create(CustomerArchives dto) {
        ok {
            dto.zch = dto.khh = ""
            customerArchivesResourceService.create(dto)
        }
    }

    @DELETE
    Response del(@QueryParam('id') Long id) {
        ok {
            customerArchivesResourceService.delete(id)
            return true
        }
    }

    @POST
    @Path('/readAllForPage')
    Response queryAll(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(CustomerArchives, arg)
            def list = dc.list(max: arg.pagesize, offset: arg.startposition)
            def result = []
            list.each {
                def r = [id: it.id]
                r.putAll(it.properties)
                result.push(r)
            }
            [data: result, total: dc.count()]
        }
    }

    @GET
    @Path('/getcustomer')
    Response getCustomer(@QueryParam('cid') Long id) {
        ok {
            CustomerArchives.get(id)
        }
    }


    @POST
    @Path('/update')
    Response update(CustomerArchives dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            return customerArchivesResourceService.update(dto)
        }
    }

    /**
     * 客户名称自动完成接口
     * @param username
     * @return
     */
    @GET
    @Path('/nameLike')
    Response findByNameLike(@QueryParam('params') String username) {
        def users = CustomerArchives.findAllByNameLike("%" + username + "%")
        JSONArray jsonArray = new JSONArray()
        users.each {
            JSONObject jso = new JSONObject()
            jso.put("value", it.name)
            jso.put("data", it.id)
            jsonArray.put(jso)
        }
        JSONObject jsonObject = new JSONObject()
        jsonObject.put("query", "Unit")
        jsonObject.put("suggestions", jsonArray)

        return Response.ok(jsonObject.toString()).build()
    }

    @GET
    @Path('/name')
    Response getCustomerByName(@QueryParam("name") String name) {
        ok {
            def user = CustomerArchives.findByNameLike(name)
            println(user)
            if (user) {
                def res = [id: user.id]
                res.putAll(user.properties)
                return  res
            }
            return null;
        }
    }

    @GET
    @Path('/bankForUserId')
    Response getUserByID(@QueryParam("id") Long id) {
        ok {
            def user = User.read(id)
            if (user == null)
                throw new Exception("No Found User!")
            print(user.chainName)
            def ca = CustomerArchives.findByNameLike(user.chainName)
            if (ca == null || ca.bankAccount.size() == 0)
                throw new Exception("此业务经理没有关联银行信息,请在客户信息中添加同名的人员信息")
            def bank = ca.bankAccount.find {
                it.defaultAccount
            }
            if (bank == null) {
                bank = ca.bankAccount.first()
            }
            [yhzh: bank.account, skr: bank.accountName, khh: bank.bankOfDeposit]
        }
    }

    @GET
    @Path('/id')
    Response getCustomerByName(@QueryParam("id") Long id) {
        ok {
            customerArchivesResourceService.read(id)
        }
    }
}
