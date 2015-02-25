package filters

import com.jsy.auth.User
import com.jsy.system.OperationRecord

class OperationRecordFilters {
    def springSecurityService
    def filters = {
        allURIs(uri:'/**') {
            before = {
                User czr=springSecurityService.getCurrentUser()
                if(!czr){
                   response.sendError(401,"请先登陆！")
                   return false
                }
                new OperationRecord(czr:czr.chainName,czsj: new Date(),url:request.request.strippedServletPath,params: params.toString(),method: request.method,address: request.getRemoteAddr() ).save(failOnError: true)
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
