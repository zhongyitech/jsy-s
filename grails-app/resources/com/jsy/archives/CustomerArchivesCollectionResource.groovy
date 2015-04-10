package com.jsy.archives

import com.jsy.utility.DomainHelper
import org.codehaus.groovy.grails.web.json.JSONObject
import org.json.JSONArray

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
    def customerArchivesResourceService

    @PUT
    Response create(CustomerArchives dto) {
        ok {
            def ca = customerArchivesResourceService.create(dto)
            print("return:ac:" + ca)
            ca
        }
    }


    @POST
    @Path('/readAllForPage')
    Response queryAll(Map arg) {
        page {
            def list = DomainHelper.getDetachedCriteria(CustomerArchives, arg)
            def result=[]
            list.collect {
                result.push(it.properties)
            }
            [data:result,total:list.count()]
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
}
