package com.jsy.project



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TSWorkflowController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TSWorkflow.list(params), model:[TSWorkflowInstanceCount: TSWorkflow.count()]
    }

    def show(TSWorkflow TSWorkflowInstance) {
        respond TSWorkflowInstance
    }

    def create() {
        respond new TSWorkflow(params)
    }

    @Transactional
    def save(TSWorkflow TSWorkflowInstance) {
        if (TSWorkflowInstance == null) {
            notFound()
            return
        }

        if (TSWorkflowInstance.hasErrors()) {
            respond TSWorkflowInstance.errors, view:'create'
            return
        }

        TSWorkflowInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'TSWorkflow.label', default: 'TSWorkflow'), TSWorkflowInstance.id])
                redirect TSWorkflowInstance
            }
            '*' { respond TSWorkflowInstance, [status: CREATED] }
        }
    }

    def edit(TSWorkflow TSWorkflowInstance) {
        respond TSWorkflowInstance
    }

    @Transactional
    def update(TSWorkflow TSWorkflowInstance) {
        if (TSWorkflowInstance == null) {
            notFound()
            return
        }

        if (TSWorkflowInstance.hasErrors()) {
            respond TSWorkflowInstance.errors, view:'edit'
            return
        }

        TSWorkflowInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TSWorkflow.label', default: 'TSWorkflow'), TSWorkflowInstance.id])
                redirect TSWorkflowInstance
            }
            '*'{ respond TSWorkflowInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(TSWorkflow TSWorkflowInstance) {

        if (TSWorkflowInstance == null) {
            notFound()
            return
        }

        TSWorkflowInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TSWorkflow.label', default: 'TSWorkflow'), TSWorkflowInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'TSWorkflow.label', default: 'TSWorkflow'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
