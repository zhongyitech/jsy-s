package com.jsy.archives

import com.jsy.fundObject.Finfo
import com.jsy.utility.DomainHelper
import com.jsy.utility.MyResponse
import grails.converters.JSON
import grails.gorm.DetachedCriteria
import org.json.JSONArray
import org.json.JSONObject

import javax.ws.rs.DefaultValue
import javax.ws.rs.QueryParam
import java.text.SimpleDateFormat

//import static org.grails.jaxrs.response.Responses.*
import static com.jsy.utility.MyResponse.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/payment')
@Consumes(['application/xml', 'application/json'])
@Produces(['application/xml', 'application/json'])
class PaymentCollectionResource {
    public static final Integer RESPONSE_STATUS_SUC = 200;
    public static final String REST_STATUS_SUC = "suc";
    public static final String REST_STATUS_FAI = "err"
    PaymentResourceService paymentResourceService

    @POST
    Response create(Payment dto, @QueryParam('id') Long id) {
        ok {
            dto.id = id
            dto = paymentResourceService.create(dto)
            dto
        }
    }

    @GET
    Response readAll() {
        ok {
            def ret = [:]
            def pc = paymentResourceService.readAll()
            ret.all = pc
            ret.total = pc.count()
            ret

        }
    }

    @POST
    @Path('/getPayments')
    Response getPayments(String datastr) {
        page {
            def ret = [:]
            int total
            def results
            org.json.JSONObject finfo = JSON.parse(datastr)

            def criterib = new DetachedCriteria(Payment).build {
                if (finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date1 = sdf.parse(finfo.startsaledate1);
                    Date date2 = sdf.parse(finfo.startsaledate2);

                    between("zfsj", date1, date2)
                }


                if (finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)) {
                    or {
                        like("fundName", "%" + finfo.keyword + "%")
                        like("customerName", "%" + finfo.keyword + "%")         //业务经理
                    }
                }
                eq("dflx", finfo.type)

                if (finfo.has('status') && finfo.status) {
                    eq("status", finfo.status)
                } else {
                    between("status", 0, 1)
                }

                order("dateCreated", "desc")
            }

            def params = [:]
            params.max = 10
            params.offset = finfo.startposition ? finfo.startposition : 0

            results = criterib.list(params)
            total = criterib.size()
            return [data: results, total: total]
        }
    }

    @POST
    @Path('/getCommissions')
    Response getCommissions(String datastr) {
        page {
            def results
            int total
            def ret = [:]
            org.json.JSONObject finfo = JSON.parse(datastr)

            def criterib = new DetachedCriteria(Payment).build {
                if (finfo.has('startsaledate1') && finfo.has('startsaledate2') && finfo.startsaledate1 && finfo.startsaledate2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
                    Date date1 = sdf.parse(finfo.startsaledate1);
                    Date date2 = sdf.parse(finfo.startsaledate2);

                    between("zfsj", date1, date2)
                }


                if (finfo.has('keyword') && finfo.keyword && !"".equals(finfo.keyword)) {
                    or {
                        like("fundName", "%" + finfo.keyword + "%")
                        like("contractNum", "%" + finfo.keyword + "%")
                        like("customerName", "%" + finfo.keyword + "%")         //业务经理
                    }
                }
                eq("dflx", finfo.type)

                if (finfo.has('status') && finfo.status) {
                    eq("status", finfo.status)
                } else {
                    between("status", 0, 1)
                }

                order("dateCreated", "desc")
            }

            def params = [:]
            params.max = 10
            params.offset = finfo.startposition ? finfo.startposition : 0
            results = criterib.list(params)
            total = criterib.size()
            return [data: results, total: total]
        }
    }

    /**
     *
     * @param ids
     * @return
     */
    @GET
    @Path('/toPay')
    Response toPay(@QueryParam('ids') String ids) {
        ok {
            def res = []
            ids.split(",").each {
                def obj = [id: it]
                Long payId = Long.valueOf(it)
                try {
                    obj.result = (paymentResourceService.updatePayment(Payment.get(payId), 1))
                } catch (Exception e) {
                    obj.resutl = false
                }
                res.add(obj)
            }
            res
        }
    }

    @Path('/{id}')
    PaymentResource getResource(@PathParam('id') Long id) {
        new PaymentResource(paymentResourceService: paymentResourceService, id: id)
    }

    @POST
    @Path('/readAllForPage')
    Response readAllForPage(Map arg) {
        MyResponse.page {
            return DomainHelper.getPage(Payment, arg)
        }
    }
}
