package com.jsy.archives

import com.jsy.utility.DomainHelper

import java.text.DecimalFormat

import static com.jsy.utility.MyResponse.*
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/contractRegister')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class ContractRegisterCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    public static int nameLength = 5
    public static int numberLength = 4
    ContractRegisterResourceService contractRegisterResourceService

    @POST
    Response create(ContractRegister dto) {

        ok {
            validBh(dto.qsbh)
            validBh(dto.jsbh)
            //截取合同编号
            int qs = Integer.parseInt(dto.qsbh.substring(nameLength))
            int js = Integer.parseInt(dto.jsbh.substring(nameLength))
            String bh = dto.qsbh.substring(0, nameLength)
            def decimalFormat = new DecimalFormat("0000")

            //验证合同编号
            for (int i = qs; i <= js; i++) {
                if (Contract.findByHtbh(bh + i)) {
                    throw new Exception("合同编号范围:" + bh + decimalFormat.format(qs) + " - " + bh + decimalFormat.format(js) + " 已经存在了")
                    break
                }
            }
            def result = contractRegisterResourceService.create(dto, qs, js, bh, decimalFormat)
            result
        }
    }

    boolean validBh(String qsbh) {
        print(nameLength)
        print("起始编号:应为" + nameLength + "位字母加" + numberLength + "位序号")
        if (qsbh == null || qsbh.length() != nameLength + numberLength) {
            throw new Exception("\"编号\"应为" + nameLength + "位字母加" + numberLength + "位序号")
        }
        return true
    }

    /**
     * 获取分布数据
     * @param arg
     * @return
     */
    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        page {
            def result = DomainHelper.getPage(ContractRegister, arg)
            return result
        }
    }

    @GET
    Response readAll() {
        ok contractRegisterResourceService.readAll()
    }

    @Path('/{id}')
    ContractRegisterResource getResource(@PathParam('id') Long id) {
        new ContractRegisterResource(contractRegisterResourceService: contractRegisterResourceService, id: id)
    }
}
