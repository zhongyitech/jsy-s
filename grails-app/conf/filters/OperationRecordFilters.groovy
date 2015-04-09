package filters

import com.jsy.auth.AuthorityService
import com.jsy.auth.OperationsAPI
import com.jsy.auth.Resource
import com.jsy.auth.ResourceRole
import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.system.OperationRecord
import com.jsy.utility.JsonResult

import javax.ws.rs.core.Response

class OperationRecordFilters {
    def springSecurityService
    AuthorityService authorityService
    def filters = {
        allURIs(uri:'/**') {
            before = {
                User czr=springSecurityService.getCurrentUser()
                if(!czr){
                   response.sendError(401,"请先登陆！")
                   return false
                }
                String url=request.request.strippedServletPath
                String method=request.method
                //访问权限检测
                OperationsAPI operationsAPI=OperationsAPI.findByMethodAndUrl(method,url)
//                if (operationsAPI){
//                    boolean b=authorityService.checkAuth(operationsAPI.resoureClass,operationsAPI.czlx)
//                    if (!b){
////                        response.sendError(402,"无此操作权限！")
//                        return Response.ok(JsonResult.error("无此操作权限")).build()
//                        return false
//                    }
//                }
                //记录操作日志
                new OperationRecord(czr:czr.chainName,czsj: new Date(),url:url,params: params.toString(),method: method,address: request.getRemoteAddr() ).save(failOnError: true)
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
