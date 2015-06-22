package com.jsy.system

import com.jsy.utility.MyResponse


import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/meta')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class MetaInitResource {

    def metaInitService


    @GET
    Response init() {
        MyResponse.ok {
            metaInitService.init()
            return true
        }
    }

}
