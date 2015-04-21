package com.jsy.archives

import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import grails.converters.JSON
import org.json.JSONObject
import sun.java2d.pipe.RegionSpanIterator

import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/filePackage')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class FilePackageCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"

    FilePackageResourceService filePackageResourceService

    /**
     * 档案入库操作
     * @param dto
     * @return
     */
    @POST
    Response create(FilePackage dto) {
        ok {
            filePackageResourceService.create(dto)
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def dc = DomainHelper.getDetachedCriteria(FilePackage, arg)
            //todo: other code
            //按分页要求返回数据格式 [数据,总页数]
            return [data: dc.list([max: arg.pagesize, offset: arg.startposition]), total: arg.startposition == 0 ? dc.count() : 0]
        }
    }

    @PUT
    Response update(FilePackage dto, @QueryParam('id') Long id) {
        ok {
            filePackageResourceService.update(dto)
        }
    }

    @GET
    @Path("/exist")
    Response exist(@QueryParam("num") String num) {
        ok {
            FilePackage.findByContractNum(num) != null
        }
    }

    @Path('/{id}')
    FilePackageResource getResource(@PathParam('id') Long id) {
        new FilePackageResource(filePackageResourceService: filePackageResourceService, id: id)
    }
}
