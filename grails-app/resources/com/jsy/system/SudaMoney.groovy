package com.jsy.system

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

/**
 * Created by lioa on 2015/3/26.
 */
@Path('/api/PublicAPI')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ExternalAPI{

    /**
     * 与速达账务软件的对接API
     */
    @GET
    @Path('/bankorder')
    Response getBankOrder(){

    }
}