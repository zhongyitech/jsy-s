package com.jsy.utility

import grails.validation.ValidationException

import javax.ws.rs.core.Response

/**
 * 替换原有 OK返回方式,对代码的异常进行标准封装
 * Created by lioa on 2015/3/20.
 */
class MyResponse {
    static Response ok(Closure closure) {
        try {
            //调用传入的代码
            def ree = closure.call()
            //封装返回结果
            return Response.ok(JsonResult.success(ree)).build()
            //对代码运行的异常进行处理
        }
        //数据入库时的字段规则验证异常
        catch (ValidationException ve) {
            def msg=""
            def result=null
            if(ve){
                msg=ve.message
                result=ve.errors
            }
            return Response.ok(JsonResult.error(msg, result)).build()
        }
        catch (Exception e) {
            //todo:write log
            return Response.ok(JsonResult.error(e.message)).build()
        }
    }

/**
 * 封闭成分页方式返回数据
 * @param closure [data:,total:]
 * @return
 */
    static Response page(Closure closure) {
        try {
            //调用传入的代码
            Map page = closure.call()
            //封装返回结果
            return Response.ok(JsonResult.success(page.data,page.total)).build()
            //对代码运行的异常进行处理
        }
        //数据入库时的字段规则验证异常
        catch (ValidationException ve) {
            return Response.ok(JsonResult.error(ve.message, ve.errors)).build()
        }
        catch (Exception e) {
            //todo:write log
            return Response.ok(JsonResult.error(e.message)).build()
        }
    }
}
