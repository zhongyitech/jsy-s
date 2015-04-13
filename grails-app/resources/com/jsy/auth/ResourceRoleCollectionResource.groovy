package com.jsy.auth


import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/resourceRole')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ResourceRoleCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def resourceRoleResourceService

    @GET
    @Path("/getRoleList")
    Response getRoleList(@QueryParam("id") Long roleId) {
        ok{
            resourceRoleResourceService.getRoleList(roleId)
        }
    }

    @POST
    @Path("/updateRoleList")
    Response updateRoleList(String data,@QueryParam("id")Long roleId,@QueryParam("resourceId")Long resourceId) {
        ok{
            resourceRoleResourceService.updateRoleList(data,roleId,resourceId)
        }
    }


    @POST
    Response create(ResourceRole dto) {
        ok{
            resourceRoleResourceService.create(dto)
        }
    }

    @GET
    Response readAll() {
        ok{
            resourceRoleResourceService.readAll()
        }
    }

    @Path('/{id}')
    ResourceRoleResource getResource(@PathParam('id') Long id) {
        new ResourceRoleResource(resourceRoleResourceService: resourceRoleResourceService, id: id)
    }

    @PUT
    Response update(ResourceRole dto,@QueryParam('id') Long id){
        ok {
            def rr
            rr = ResourceRole.get(id)
            rr.operations = dto.operations
            rr.propertys = dto.propertys
            rr.save(failOnError: true)
            rr
        }
//        JSONObject result = new JSONObject();
//        String restStatus = REST_STATUS_SUC;
//        def rr
//        try {
//            rr = ResourceRole.get(id)
//            rr.operations = dto.operations
//            rr.propertys = dto.propertys
//            rr.save(failOnError: true)
//            result.put("rest_status", restStatus)
//            result.put("rest_result", rr as JSON)
//            return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
//        }catch (Exception e){
//            restStatus = REST_STATUS_FAI
//            print(e)
//            e.printStackTrace()
//            result.put("rest_status", restStatus)
//            result.put("rest_result", rr as JSON)
//            return Response.ok(result.toString()).status(500).build()
//        }
    }
}
