import com.jsy.auth.AuthenticationToken
import com.jsy.auth.Property
import com.jsy.auth.Resource
import com.jsy.auth.ResourceRole
import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.system.Department
import com.jsy.system.TodoConfig
import com.jsy.system.TypeConfig
import com.jsy.project.TSProject
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        init_metaExtend();
        def users=User.findAllByUsername('admin')
        if(users&&users.size()>0){
            return;
        }
        new AuthenticationToken(username: 'admin', token: '123123123').save(failOnError: true)
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy/MM/dd HH:mm:ss")
        }

//        def users = User.findAllByUsername('')
        //部门的职能
        TypeConfig performance1 = new TypeConfig(type:8,mapName: "管理",mapValue: 1,description: "默认职能")
        performance1.save(failOnError: true)
        TypeConfig performance2 = new TypeConfig(type:8,mapName: "销售",mapValue: 2,description: "基金销售部门选择此职能")
        performance2.save(failOnError: true)
//        new TypeConfig(type:8,mapName: "其它",mapValue: 3).save(failOnError: true)
        Department department=new Department(deptName:"销售部",buildDate:new Date(),performance:performance2)
        department.save(failOnError: true)
        Department department2=new Department(deptName:"行政管理部",buildDate:new Date(),performance:performance1)
        department2.save(failOnError: true)


//        /* 添加角色数据  */
//        [
//                new Role(name: '总监'),new Role(name: '经理')
//        ]
//        .each {
//            it.save(flush: true)
//        }

        def user1 = User.findByUsername('admin') ?: new User(
                skr:'oswaldl',
                khh:'平安银行',
                yhzh:'136541615646156',
                username: 'admin',
                password: 'admin',
                department:department,
                chainName: "张三",
                enabled: true).save(flush: true)
        def user2 = User.findByUsername('pengyh') ?: new User(
                skr:'rela',
                khh:'平安银行',
                yhzh:'52624623',
                username: 'zhangj',
                chainName: '张五',
                password: 'zhangj',
                department:department,
                enabled: true).save(flush: true)
        def user3 = User.findByUsername('liujw') ?: new User(
                skr:'zbua',
                khh:'平安银行',
                yhzh:'436461352352',
                username: 'liujw',
                chainName: '刘六',
                password: 'liujw',
                department:department,
                enabled: true).save(flush: true)
        def user4 = User.findByUsername('li') ?: new User(
                skr:'kgs',
                khh:'平安银行',
                yhzh:'465742632',
                username: 'li',
                chainName: '李四',
                password: 'li',
                department:department,
                enabled: true).save(flush: true)


        //角色
        def userRole = Role.findByAuthority('ROLE_USER')
        if (!userRole) {
            userRole = new Role(authority: 'ROLE_USER', name: 'UserRole' )
            userRole.save(flush: true)
        }

        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        if (!adminRole) {
            adminRole = new Role(authority: 'ROLE_ADMIN', name: 'AdminRole')
            adminRole.save(failOnError: true)
        }
        def managerRole = Role.findByAuthority('ROLE_MANAGER')
        if (!managerRole) {
            managerRole = new Role(authority: 'ROLE_MANAGER', name: '部门经理')
            managerRole.save(failOnError: true)
        }
        def manager1 = Role.findByAuthority('ROLE_MANAGER1')
        if (!manager1) {
            manager1 = new Role(authority: 'ROLE_MANAGER1', name: '业务经理')
            manager1.save(failOnError: true)
        }

        def manager2 = Role.findByAuthority('ROLE_MANAGER2')
        if (!manager2) {
            manager2 = new Role(authority: 'ROLE_MANAGER2', name: '提成查询')
            manager2.save(failOnError: true)
        }
        def manager3 = Role.findByAuthority('ROLE_MANAGER3')
        if (!manager3) {
            manager3 = new Role(authority: 'ROLE_MANAGER3', name: '兑付查询')
            manager3.save(failOnError: true)
        }

        new TodoConfig(mkbs:1,mkmc:"提成查询",cljs:manager2,url: "www.baidu.com").save(failOnError: true)
        new TodoConfig(mkbs:2,mkmc:"兑付查询",cljs:manager3,url: "www.baidu.com").save(failOnError: true)

        if (!user3.authorities.contains(adminRole)) {
            UserRole.create user3, adminRole
        }
        if (!user1.authorities.contains(adminRole)) {
            UserRole.create user1, adminRole
        }
        if (!user1.authorities.contains(managerRole)) {
            UserRole.create user1, managerRole
        }
        if (!user2.authorities.contains(manager1)) {
            UserRole.create user2, manager1
        }
        if (!user4.authorities.contains(manager1)) {
            UserRole.create user4, manager1
        }
        if (!user4.authorities.contains(manager2)) {
            UserRole.create user4, manager2
        }
        if (!user4.authorities.contains(manager3)) {
            UserRole.create user4, manager3
        }
        //基金状态
        new TypeConfig(type: 1,mapName:"待售",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 1,mapName:"在售",mapValue: 2).save(failOnError: true)
        new TypeConfig(type: 1,mapName:"售罄",mapValue: 3).save(failOnError: true)
        //类型
        new TypeConfig(type: 2,mapName:"机密",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 2,mapName:"公开",mapValue: 2).save(failOnError: true)
        //档案类型
        new TypeConfig(type: 3,mapName:"基金认购合同",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 3,mapName:"项目合作合同",mapValue: 2).save(failOnError: true)
        new TypeConfig(type: 3,mapName:"项目总结资料",mapValue: 3).save(failOnError: true)
        //档案室
        new TypeConfig(type: 4,mapName:"机要室",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 4,mapName:"公办室",mapValue: 2).save(failOnError: true)
        //状态
        new TypeConfig(type: 5,mapName:"在库",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 5,mapName:"外借",mapValue: 2).save(failOnError: true)
        new TypeConfig(type: 5,mapName:"遗失",mapValue: 3).save(failOnError: true)
        new TypeConfig(type: 5,mapName:"销毁",mapValue: 4).save(failOnError: true)
        //公司类型
        new TypeConfig(type: 6,mapName:"普通公司",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 6,mapName:"有限合伙",mapValue: 2).save(failOnError: true)
        //银行类型
        new TypeConfig(type: 7,mapName:"兑付",mapValue: 1).save(failOnError: true)
        new TypeConfig(type: 7,mapName:"日常支出",mapValue: 2).save(failOnError: true)
        new TypeConfig(type: 7,mapName:"募集",mapValue: 3).save(failOnError: true)
        new TypeConfig(type: 7,mapName:"其它",mapValue: 4).save(failOnError: true)



        //权限数据写入
        Resource jj=new Resource(name:"基金",objectName: "Fund").save(failOnError: true)
        Resource da=new Resource(name:"档案",objectName: "InvestmentArchives").save(failOnError: true)
        Resource kh=new Resource(name:"客户",objectName: "Customer").save(failOnError: true)
        Resource yh=new Resource(name:"用户",objectName: "User").save(failOnError: true)

        //User资源
        ResourceRole resourceRole=new ResourceRole(role: adminRole,resource:yh).save(failOnError: true)
        Property property1=new Property(name:'username',title:'用户名',visible:true).save(failOnError: true)
        Property property2=new Property(name:'chainName',title:'中文名',visible:true).save(failOnError: true)
        Property property3=new Property(name:'skr',title:'收款人',visible:false).save(failOnError: true)
        Property property4=new Property(name:'khh',title:'开户行',visible:false).save(failOnError: true)
        Property property5=new Property(name:'yhzh',title:'银行账户',visible:false).save(failOnError: true)
        resourceRole.addToPropertys(property1)
        resourceRole.addToPropertys(property2)
        resourceRole.addToPropertys(property3)
        resourceRole.addToPropertys(property4)
        resourceRole.addToPropertys(property5)
        //基金资源
        ResourceRole resourceRole1=new ResourceRole(role: adminRole,resource:jj).save(failOnError: true)
        Property property11=new Property(name:'fundName',title:'基金名',visible:true).save(failOnError: true)
        Property property12=new Property(name:'fundNo',title:'基金编号',visible:true).save(failOnError: true)
        Property property13=new Property(name:'raiseFunds',title:'预募规模',visible:true).save(failOnError: true)
        Property property14=new Property(name:'khh',title:'开户行',visible:true).save(failOnError: true)
        Property property15=new Property(name:'yhzh',title:'银行账户',visible:true).save(failOnError: true)
        resourceRole1.addToPropertys(property11)
        resourceRole1.addToPropertys(property12)
        resourceRole1.addToPropertys(property13)
        resourceRole1.addToPropertys(property14)
        resourceRole1.addToPropertys(property15)
        //客户资源

    }


    def destroy = {


    }



    def init_metaExtend = {
        println "init_metaExtend start"
        Object.metaClass.union = { obj->
            obj.properties.each { prop, val ->
                if(prop in ["metaClass","class"]) return
                if(delegate.delegate.hasProperty(prop) && val){
                    if(delegate.delegate.hasProperty(prop).setter){
                        delegate.delegate[prop] = val
                    }else{

                    }

                }
            }
        }

        Object.metaClass.unionMap = { map->
            map.each {
                if(delegate.delegate.hasProperty(it.key)){
                    if(delegate.delegate.hasProperty(it.key).setter){
                        delegate.delegate[it.key] = it.value
                    }else{

                    }
                }
            }
        }
    }
}
