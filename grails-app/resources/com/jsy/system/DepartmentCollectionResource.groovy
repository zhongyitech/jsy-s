package com.jsy.system

import com.jsy.fundObject.FundCompanyInformation
import com.jsy.utility.DomainHelper
import com.jsy.utility.MyResponse
import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.web.json.JSONObject

import javax.ws.rs.DELETE
import javax.ws.rs.DefaultValue
import javax.ws.rs.PUT
import javax.ws.rs.QueryParam


import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/department')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class DepartmentCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    def departmentResourceService
    private static final log = LogFactory.getLog(this)

    /**
     * 创建部门信息
     * @param dto
     * @return
     */
    @PUT
    Response create(Department dto) {
        MyResponse.ok {
            departmentResourceService.create(dto)
        }
    }

    /**
     * 更新部门的信息
     * @param dto
     * @param id
     * @return
     */
    @POST
    Response update(Department dto, @QueryParam('id') Long id) {
        MyResponse.ok {
            if (dto.parent == id) {
                throw new Exception("上级部门不能是自己")
            }
            print(dto.leader)
            if (dto.leader && dto.leader.department && dto.leader.department.id != id) {
                throw new Exception("负责人必须是本部门人员")
            }
            dto.id = id
            departmentResourceService.update(dto)
        }
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {

        MyResponse.page {
            log.info("get List")
            def 开户行 = "1214"
            def s = "${开户行}[账号后4位]"
            print(s)
            DomainHelper.getPage(Department, arg)
        }
    }

    /**
     * 返回select 列表
     * @param depId 过滤某一部门(ID)
     * @return
     */
    @GET
    @Path('/selectList')
    Response seleectList(@QueryParam('depId') @DefaultValue('0') Long depId,
                         @QueryParam('pid') @DefaultValue('0') Long pid) {
        MyResponse.ok {
            print(depId + "," + pid)
            def dc = Department.createCriteria()
            def list = dc.list {
                if (depId > 0) {
                    ne('id', depId)
                }
                if (pid > 0) {
                    def company = FundCompanyInformation.get(pid)
                    eq('fundCompanyInformation', company)
                }
            }
            list.collect {
                [mapName: it.deptName + " | " + it.fundCompanyInformation?.companyName, id: it.id]
            }
        }
    }

    @GET
    @Path('/id')
    Response getDempartment(@QueryParam('id') Long id) {
        MyResponse.ok {
            departmentResourceService.read(id)
        }
    }

    @GET
    @Path('/findByName')
    Response findByName(@QueryParam('parm') String parm) {
        JSONObject result = new JSONObject();
        String restStatus = REST_STATUS_SUC;
        def dp
        try {
            dp = Department.findAllByDeptNameLike(parm)

        } catch (Exception e) {
            restStatus = REST_STATUS_FAI
            print(e)
        }
        result.put("rest_status", restStatus)
        result.put("rest_result", dp as JSON)
        return Response.ok(result.toString()).status(RESPONSE_STATUS_SUC).build()
    }

    @DELETE
    Response delete(@QueryParam('id') Long id) {
        MyResponse.ok {
            departmentResourceService.delete(id)
        }
    }

    @Path('/{id}')
    DepartmentResource getResource(@PathParam('id') Long id) {
        new DepartmentResource(departmentResourceService: departmentResourceService, id: id)
    }
}
