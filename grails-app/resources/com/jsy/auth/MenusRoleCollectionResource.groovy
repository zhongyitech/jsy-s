package com.jsy.auth

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray

import javax.ws.rs.QueryParam

import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/menusRole')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class MenusRoleCollectionResource {

    def menusRoleResourceService

    @GET
    @Path('/getMenus')
    Response getMenus() {
        ok {
            menusRoleResourceService.getMenus()
        }
    }

    @GET
    @Path('/getMenuList')
    Response getMenuList(@QueryParam("id")Long roleId) {
        ok {
            menusRoleResourceService.getMenuList(roleId)
        }
    }

    @POST
    @Path('/updateMenuRole')
    Response updateMenuRole(String data,@QueryParam("id")Long roleId) {
        ok {
            menusRoleResourceService.updateMenuRole(data,roleId)
        }
    }

    @POST
    Response create(MenusRole dto) {
        ok {
            menusRoleResourceService.create(dto)
        }
    }

    @GET
    Response readAll() {
        ok {
            menusRoleResourceService.readAll()
        }
    }

    @Path('/{id}')
    MenusRoleResource getResource(@PathParam('id') Long id) {
        new MenusRoleResource(menusRoleResourceService: menusRoleResourceService, id: id)
    }
}
