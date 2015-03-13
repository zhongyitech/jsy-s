package com.jsy.system

import javax.ws.rs.Consumes
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response

import static org.grails.jaxrs.response.Responses.ok

/**
 * Created by William.Wei on 2015/3/12.
 */
@Path('/api/reflect')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class ReflectResource {
    def reflectService
    @GET
    Response getObject(@QueryParam("id") @DefaultValue("") String id,@QueryParam("className") @DefaultValue("") String className,@QueryParam("fields")  @DefaultValue("") String fields) {
        ok reflectService.getObject(id,className,fields);
    }
}