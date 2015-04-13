package com.jsy.auth

import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Transactional(rollbackFor = Throwable.class)
class ResourceRoleResourceService {

    def getRoleList(Long roleId){
        def resourceList=Resource.findAll()
        def propertyIds=[:]
        def operationIds=[:]
        ResourceRole.findAllByRole(Role.get(roleId)).each {
            def propsArr=propertyIds[it.resource.id]=[]
            def opsArr=operationIds[it.resource.id]=[]
            it.propertys.each {
                propsArr.push(it.id)
            }
            it.operations.each{
                opsArr.push(it.id)
            }
        }
        def result=[]
        resourceList.each {
            resource->
                def resourceId=resource.id
                def props=propertyIds[resourceId]
                props=props?props:[]
                def ops=operationIds[resourceId]
                ops=ops?ops:[]
                def row=[
                        props:[],
                        ops:[],
                        name:resource.name,
                        id: resourceId
                ]
                resource.propertys.each {
                    def property=[id:it.id,checked:props.contains(it.id)]
                    property.putAll(it.properties)
                    row.props.push(property)
                }
                resource.operations.each{
                    def property=[id:it.id,checked:ops.contains(it.id)]
                    property.putAll(it.properties)
                    row.ops.push(property)
                }
                result.push(row)
        }
        return result
    }

    def updateRoleList(String data,Long roleId,Long resourceId){
        def object = JSON.parse(data)
        def role=Role.get(roleId)
        def resource=Resource.get(resourceId)
        def oldResourceRole=ResourceRole.findByRoleAndResource(role,resource)
        if(oldResourceRole){
            oldResourceRole.delete(flush: true)
        }
        def resourceRole=new ResourceRole(role: role,resource: resource)
        if(object.props){
            object.props.each {
                Long id=it.id
                resourceRole.addToPropertys(Property.get(id))
            }
        }
        if(object.ops){
            object.ops.each{
                Long id=it.id
                resourceRole.addToOperations(Operation.get(id))
            }
        }
        resourceRole.save(failOnError: true)
        return data
    }

    def create(ResourceRole dto) {
        dto.save()
    }

    def read(id) {
        def obj = ResourceRole.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ResourceRole.class, id)
        }
        obj
    }

    def readAll() {
        ResourceRole.findAll()
    }

    def update(ResourceRole dto) {
        def obj = ResourceRole.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(ResourceRole.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ResourceRole.get(id)
        if (obj) {
            obj.delete()
        }
    }
}
