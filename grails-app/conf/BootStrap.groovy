import com.jsy.PublicAPI.SudaMoney
import com.jsy.archives.CustomerArchives
import com.jsy.archives.InvestmentArchives
import com.jsy.auth.AuthenticationToken
import com.jsy.auth.Menus
import com.jsy.auth.MenusRole
import com.jsy.auth.Operation
import com.jsy.auth.OperationsAPI
import com.jsy.auth.Property
import com.jsy.auth.Resource
import com.jsy.auth.ResourceRole
import com.jsy.auth.Role
import com.jsy.auth.User
import com.jsy.auth.UserRole
import com.jsy.customerObject.Customer
import com.jsy.fundObject.Fund
import com.jsy.fundObject.FundCompanyInformation
import com.jsy.project.SpecailAccess
import com.jsy.project.TSWorkflowModel
import com.jsy.project.TSWorkflowModelPhase
import com.jsy.system.Department
import com.jsy.system.TodoConfig
import com.jsy.system.TypeConfig
import com.jsy.project.TSProject
import grails.converters.JSON

import java.text.DateFormat
import java.text.SimpleDateFormat

class BootStrap {

    def init = {
        servletContext ->
            init_metaExtend();
            initFlowModel();

            def users = User.findAllByUsername('admin')
            if (users && users.size() > 0) {
                return;
            }
            new AuthenticationToken(username: 'admin', token: '123123123').save(failOnError: true)

//        def users = User.findAllByUsername('')
            //部门的职能

            new AuthenticationToken( token: SudaMoney.ApiToken_Suda,username:"APIUSER").save(failOnError: true)


            new TypeConfig(type: 1, mapName: "待售", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 1, mapName: "在售", mapValue: 2).save(failOnError: true)
            new TypeConfig(type: 1, mapName: "售罄", mapValue: 3).save(failOnError: true)
            //类型
            new TypeConfig(type: 2, mapName: "机密", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 2, mapName: "公开", mapValue: 2).save(failOnError: true)
            //档案类型
            new TypeConfig(type: 3, mapName: "基金认购合同", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 3, mapName: "项目合作合同", mapValue: 2).save(failOnError: true)
            new TypeConfig(type: 3, mapName: "项目总结资料", mapValue: 3).save(failOnError: true)
            //档案室
            new TypeConfig(type: 4, mapName: "机要室", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 4, mapName: "公办室", mapValue: 2).save(failOnError: true)
            //状态
            new TypeConfig(type: 5, mapName: "在库", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 5, mapName: "外借", mapValue: 2).save(failOnError: true)
            new TypeConfig(type: 5, mapName: "遗失", mapValue: 3).save(failOnError: true)
            new TypeConfig(type: 5, mapName: "销毁", mapValue: 4).save(failOnError: true)
            //公司类型
            new TypeConfig(type: 6, mapName: "普通公司", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 6, mapName: "有限合伙", mapValue: 2).save(failOnError: true)
            //银行类型
            new TypeConfig(type: 7, mapName: "兑付", mapValue: 1).save(failOnError: true)
            new TypeConfig(type: 7, mapName: "日常支出", mapValue: 2).save(failOnError: true)
            new TypeConfig(type: 7, mapName: "募集", mapValue: 3).save(failOnError: true)
            new TypeConfig(type: 7, mapName: "其它", mapValue: 4).save(failOnError: true)
            TypeConfig performance1 = new TypeConfig(type: 8, mapName: "管理", mapValue: 1, description: "默认职能")
            performance1.save(failOnError: true)
            TypeConfig performance2 = new TypeConfig(type: 8, mapName: "销售", mapValue: 2, description: "基金销售部门选择此职能")
            performance2.save(failOnError: true)

            //add Company
            def defaultCompany = new FundCompanyInformation(companyName: "深圳金赛银基金管理有限公司", companyNickName: "JSY", telephone: "0755-8888888", fax: "0755-8888888"
                    , city: '深圳市', address: '深圳市罗湖区', area: '罗湖区', corporate: "王", foundingDate: new Date(), province: "广东省", status: "正常", companyType:
                    TypeConfig.findByTypeAndMapValue(6, 1), protocolTemplate: 0)
                    .save(failOnError: true)
            def ceoDeparment = new Department(deptName: "董事会", performance: performance1, fundCompanyInformation: defaultCompany, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)

            new Department(deptName: "总裁办公室", performance: performance1, parent: ceoDeparment, fundCompanyInformation: defaultCompany, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "投资决策委员会", performance: performance1, parent: ceoDeparment, fundCompanyInformation: defaultCompany, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "战略发展委员会", performance: performance1, parent: ceoDeparment, fundCompanyInformation: defaultCompany, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "薪酬管理委员会", performance: performance1, parent: ceoDeparment, fundCompanyInformation: defaultCompany, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)


            def defaultDeparment1 = new Department(deptName: "账务管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "资金管理部", parent: defaultDeparment1, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "会计核算部", parent: defaultDeparment1, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "财务结算部", parent: defaultDeparment1, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)



            def defaultDeparment2 = new Department(deptName: "销售管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "财务结算部", parent: defaultDeparment2, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            Department department = new Department(deptName: "财富一部", parent: defaultDeparment2, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "财富二部", parent: defaultDeparment2, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "财富三部", parent: defaultDeparment2, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "客服部", parent: defaultDeparment2, fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)



            def defaultDeparment3 = new Department(deptName: "分子公司管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            Department department2 = new Department(deptName: "行政管理部", buildDate: new Date(), performance: performance1, parent: defaultDeparment2)
            department2.save(failOnError: true)
            new Department(deptName: "分子公司管理部", parent: defaultDeparment3, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "招商管理部", parent: defaultDeparment3, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)


            def defaultDeparment4 = new Department(deptName: "企划管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "市场策划部", parent: defaultDeparment4, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "公共媒体部", parent: defaultDeparment4, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "品牌推广部", parent: defaultDeparment4, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "金赛银财富商学院", parent: defaultDeparment4, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)


            def defaultDeparment5 = new Department(deptName: "项目管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "项目前期部", parent: defaultDeparment5, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "资产管理部", parent: defaultDeparment5, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "投资银行部", parent: defaultDeparment5, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "基金方案部", parent: defaultDeparment5, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)

            def defaultDeparment6 = new Department(deptName: "资本管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "直投项目部", parent: defaultDeparment6, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "海外投资部", parent: defaultDeparment6, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "关联投资部", parent: defaultDeparment6, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)


            def defaultDeparment7 = new Department(deptName: "行政管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "人力资源部", parent: defaultDeparment7, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "行政综合部", parent: defaultDeparment7, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "信息技术部", parent: defaultDeparment7, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "档案管理部", parent: defaultDeparment7, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)

            def defaultDeparment8 = new Department(deptName: "风控管理中心", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "合规风控部", parent: defaultDeparment8, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "稽核审计部", parent: defaultDeparment8, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "外包服务部", parent: defaultDeparment8, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "投后管理部", parent: defaultDeparment8, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "境外律师部", parent: defaultDeparment8, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)
            new Department(deptName: "金融产品发行部", parent: defaultDeparment8, fundCompanyInformation: defaultCompany, performance: performance2, type: 0, description: "", status: 0, buildDate: new Date()).save(failOnError: true)

            def adminDeparment = new Department(deptName: "系统测试部", fundCompanyInformation: defaultCompany, performance: performance1, type: 0, description: "负责系统功能的测试和开发部门(Dev Test)", status: 0, buildDate: new Date()).save(failOnError: true)

//        new TypeConfig(type:8,mapName: "其它",mapValue: 3).save(failOnError: true)
//            Department department = new Department(deptName: "销售部", buildDate: new Date(), performance: performance2)
//            department.save(failOnError: true)

//        /* 添加角色数据  */
//        [
//                new Role(name: '总监'),new Role(name: '经理')
//        ]
//        .each {
//            it.save(flush: true)
//        }

            def user1 = User.findByUsername('admin') ?: new User(
                    skr: 'oswaldl',
                    khh: '平安银行',
                    yhzh: '8888888888',
                    username: 'admin',
                    password: 'admin',
                    department: adminDeparment,
                    chainName: "管理员(Test)",
                    enabled: true).save(flush: true)
            adminDeparment.leader = user1
            adminDeparment.save(failOnError: true)

            def ordinary = User.findByUsername('ordinary') ?: new User(
                    skr: 'oswaldl',
                    khh: '平安银行',
                    yhzh: '888888888888',
                    username: 'user1',
                    password: '123',
                    department: department,
                    chainName: "业务经理(Test)",
                    enabled: true).save(flush: true)
            def user2 = User.findByUsername('pengyh') ?: new User(
                    skr: '部门经理',
                    khh: '平安银行',
                    yhzh: '123456789',
                    username: 'user2',
                    chainName: '部门经理(Test)',
                    password: '123',
                    department: department,
                    enabled: true).save(flush: true)
            department.leader = user2
            department.save(failOnError: true)
            def user3 = User.findByUsername('liujw') ?: new User(
                    skr: '普通员工',
                    khh: '平安银行',
                    yhzh: '436461352352',
                    username: 'user3',
                    chainName: '普通员工(Test)',
                    password: '123',
                    department: adminDeparment,
                    enabled: true).save(flush: true)
            def user4 = User.findByUsername('li') ?: new User(
                    skr: '财务人员',
                    khh: '平安银行',
                    yhzh: '465742632',
                    username: 'user4',
                    chainName: '财务人员(Test)',
                    password: '123',
                    department: adminDeparment,
                    enabled: true).save(flush: true)
            def user5 = User.findByUsername('li') ?: new User(
                    skr: '行政人员',
                    khh: '平安银行',
                    yhzh: '465742632',
                    username: 'user5',
                    chainName: '行政人员(Test)',
                    password: '123',
                    department: adminDeparment,
                    enabled: true).save(flush: true)

            //角色
            def userRole = Role.findByAuthority('ROLE_USER')
            if (!userRole) {
                userRole = new Role(authority: 'ROLE_USER', name: 'UserRole')
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

            new TodoConfig(mkbs: 1, mkmc: "提成查询", cljs: manager2, url: "www.baidu.com").save(failOnError: true)
            new TodoConfig(mkbs: 2, mkmc: "兑付查询", cljs: manager3, url: "www.baidu.com").save(failOnError: true)

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
            if (!ordinary.authorities.contains(userRole)) {
                UserRole.create ordinary, userRole
            }
            //基金状态

            //权限数据写入
            Resource jj = new Resource(name: "基金", objectName: Fund.class.getName()).save(failOnError: true)
            Resource da = new Resource(name: "档案", objectName: InvestmentArchives.class.getName()).save(failOnError: true)
            Resource kh = new Resource(name: "客户", objectName: Customer.class.getName()).save(failOnError: true)
            Resource yh = new Resource(name: "用户", objectName: User.class.getName()).save(failOnError: true)

            //Common操作权限
            Operation operation1 = new Operation(cz: 'read', name: '查询', title: '查询', visible: true).save(failOnError: true)
            Operation operation2 = new Operation(cz: 'creat', name: '创建', title: '创建', visible: true).save(failOnError: true)
            Operation operation3 = new Operation(cz: 'update', name: '更新', title: '更新', visible: true).save(failOnError: true)
            Operation operation4 = new Operation(cz: 'delete', name: '删除', title: '删除', visible: true).save(failOnError: true)

            Property property0 = new Property(name: 'id', title: '用户id', visible: true).save(failOnError: true)
            Property property1 = new Property(name: 'username', title: '用户名', visible: true).save(failOnError: true)
            Property property2 = new Property(name: 'chainName', title: '中文名', visible: true).save(failOnError: true)
            Property property3 = new Property(name: 'skr', title: '收款人', visible: true).save(failOnError: true)
            Property property4 = new Property(name: 'khh', title: '开户行', visible: true).save(failOnError: true)
            Property property5 = new Property(name: 'yhzh', title: '银行账户', visible: true).save(failOnError: true)
            yh.addToPropertys(property0)
            yh.addToPropertys(property1)
            yh.addToPropertys(property2)
            yh.addToPropertys(property3)
            yh.addToPropertys(property4)
            yh.addToPropertys(property5)

            yh.addToOperations(operation1)
            yh.addToOperations(operation2)
            yh.addToOperations(operation3)
            yh.addToOperations(operation4)

            //User资源
            ResourceRole resourceRole = new ResourceRole(role: adminRole, resource: yh).save(failOnError: true)

            ResourceRole resourceRoleUR = new ResourceRole(role: userRole, resource: yh).save(failOnError: true)
            //基金资源
            ResourceRole resourceRole1 = new ResourceRole(role: adminRole, resource: jj).save(failOnError: true)

            Property property10 = new Property(name: 'id', title: '基金id', visible: true).save(failOnError: true)
            Property property11 = new Property(name: 'fundName', title: '基金名', visible: true).save(failOnError: true)
            Property property12 = new Property(name: 'fundNo', title: '基金编号', visible: true).save(failOnError: true)
            Property property13 = new Property(name: 'raiseFunds', title: '预募规模', visible: true).save(failOnError: true)
            Property property14 = new Property(name: 'yieldRange', title: '收益率范围', visible: true).save(failOnError: true)
            Property property15 = new Property(name: 'tcfpfw', title: '提成分配', visible: true).save(failOnError: true)
            Property property16 = new Property(name: 'kxzqx', title: '可选择期限', visible: true).save(failOnError: true)
            Property property17 = new Property(name: 'status', title: '基金状态', visible: true).save(failOnError: true)
            jj.addToPropertys(property10)
            jj.addToPropertys(property11)
            jj.addToPropertys(property12)
            jj.addToPropertys(property13)
            jj.addToPropertys(property14)
            jj.addToPropertys(property15)
            jj.addToPropertys(property16)
            jj.addToPropertys(property17)
            jj.addToOperations(operation1)
            jj.addToOperations(operation2)
            jj.addToOperations(operation3)
            jj.addToOperations(operation4)

            //客户资源

            ResourceRole resourceRole2 = new ResourceRole(role: adminRole, resource: kh).save(failOnError: true)
            Property property20 = new Property(name: 'id', title: '客户id', visible: true).save(failOnError: true)
            Property property21 = new Property(name: 'name', title: '客户名', visible: true).save(failOnError: true)
            Property property22 = new Property(name: 'country', title: '国家（地区）', visible: true).save(failOnError: true)
            Property property23 = new Property(name: 'credentialsType', title: '证照类型', visible: true).save(failOnError: true)
            Property property24 = new Property(name: 'credentialsNumber', title: '证件号码', visible: true).save(failOnError: true)
            Property property25 = new Property(name: 'credentialsAddr', title: '身份证地址', visible: true).save(failOnError: true)
            Property property26 = new Property(name: 'fddbr', title: '法定代表人', visible: true).save(failOnError: true)
            Property property27 = new Property(name: 'zch', title: '注册号', visible: true).save(failOnError: true)
            Property property28 = new Property(name: 'khh', title: '开户行', visible: true).save(failOnError: true)
            Property property29 = new Property(name: 'telephone', title: '联系电话', visible: true).save(failOnError: true)
            Property property291 = new Property(name: 'phone', title: '联系手机', visible: true).save(failOnError: true)
            Property property292 = new Property(name: 'postalcode', title: '邮政编码', visible: true).save(failOnError: true)
            Property property293 = new Property(name: 'callAddress', title: '通讯地址', visible: true).save(failOnError: true)
            Property property294 = new Property(name: 'email', title: '邮箱', visible: true).save(failOnError: true)
            Property property295 = new Property(name: 'remark', title: '备注', visible: true).save(failOnError: true)
            Property property296 = new Property(name: 'bankAccount', title: '银行账户', visible: true).save(failOnError: true)
            Property property297 = new Property(name: 'uploadFiles', title: '客户附件', visible: true).save(failOnError: true)
            kh.addToPropertys(property20)
            kh.addToPropertys(property21)
            kh.addToPropertys(property22)
            kh.addToPropertys(property23)
            kh.addToPropertys(property24)
            kh.addToPropertys(property25)
            kh.addToPropertys(property26)
            kh.addToPropertys(property27)
            kh.addToPropertys(property28)
            kh.addToPropertys(property29)
            kh.addToPropertys(property291)
            kh.addToPropertys(property292)
            kh.addToPropertys(property293)
            kh.addToPropertys(property294)
            kh.addToPropertys(property295)
            kh.addToPropertys(property296)
            kh.addToPropertys(property297)
            //operation
            kh.addToOperations(operation1)
            kh.addToOperations(operation2)
            kh.addToOperations(operation3)
            kh.addToOperations(operation4)
            //档案资源
            ResourceRole resourceRole3 = new ResourceRole(role: adminRole, resource: da).save(failOnError: true)
            Property property01 = new Property(name: 'id', title: '档案id', visible: true).save(failOnError: true)
            Property property31 = new Property(name: 'markNum', title: '档案编号', visible: true).save(failOnError: true)
            Property property32 = new Property(name: 'archiveNum', title: '编号', visible: true).save(failOnError: true)
            Property property33 = new Property(name: 'contractNum', title: '合同编号', visible: true).save(failOnError: true)
            Property property34 = new Property(name: 'fund', title: '基金', visible: true).save(failOnError: true)
            Property property35 = new Property(name: 'tzje', title: '投资金额', visible: true).save(failOnError: true)
            Property property36 = new Property(name: 'tzqx', title: '投资期限', visible: true).save(failOnError: true)
            Property property37 = new Property(name: 'rgrq', title: '认购日期', visible: true).save(failOnError: true)
            Property property38 = new Property(name: 'dqrq', title: '到期日期', visible: true).save(failOnError: true)
            Property property39 = new Property(name: 'fxfs', title: '付息方式', visible: true).save(failOnError: true)
            Property property30 = new Property(name: 'nhsyl', title: '年化收益率', visible: true).save(failOnError: true)
            Property property311 = new Property(name: 'htzt', title: '合同状态', visible: true).save(failOnError: true)
            Property property321 = new Property(name: 'dabz', title: '备注', visible: true).save(failOnError: true)
            Property property331 = new Property(name: 'bmjl', title: '部门经理', visible: true).save(failOnError: true)
            Property property341 = new Property(name: 'ywjl', title: '业务经理', visible: true).save(failOnError: true)
            Property property351 = new Property(name: 'bm', title: '部门', visible: true).save(failOnError: true)
            Property property361 = new Property(name: 'gltc', title: '管理提成点', visible: true).save(failOnError: true)
            Property property371 = new Property(name: 'ywtc', title: '业务提成点', visible: true).save(failOnError: true)
            Property property381 = new Property(name: 'description', title: '备注说明', visible: true).save(failOnError: true)
            Property property391 = new Property(name: 'username', title: '客户名', visible: true).save(failOnError: true)
            Property property301 = new Property(name: 'dycs', title: '打印次数', visible: true).save(failOnError: true)
            Property property312 = new Property(name: 'zjdysj', title: '最近打印时间', visible: true).save(failOnError: true)
            Property property322 = new Property(name: 'bj', title: '本金', visible: true).save(failOnError: true)
            Property property332 = new Property(name: 'dazt', title: '档案', visible: true).save(failOnError: true)
            Property property342 = new Property(name: 'status', title: '档案兑付状态', visible: true).save(failOnError: true)
            Property property352 = new Property(name: 'ywtcs', title: '业务提成', visible: true).save(failOnError: true)
            Property property362 = new Property(name: 'gltcs', title: '管理提成', visible: true).save(failOnError: true)
            Property property372 = new Property(name: 'uploadFiles', title: '档案附件', visible: true).save(failOnError: true)
            Property property382 = new Property(name: 'payTimes', title: '兑付时间', visible: true).save(failOnError: true)
            da.addToPropertys(property01)
            da.addToPropertys(property31)
            da.addToPropertys(property32)
            da.addToPropertys(property33)
            da.addToPropertys(property34)
            da.addToPropertys(property35)
            da.addToPropertys(property36)
            da.addToPropertys(property37)
            da.addToPropertys(property38)
            da.addToPropertys(property39)
            da.addToPropertys(property30)
            da.addToPropertys(property311)
            da.addToPropertys(property321)
            da.addToPropertys(property331)
            da.addToPropertys(property341)
            da.addToPropertys(property351)
            da.addToPropertys(property361)
            da.addToPropertys(property371)
            da.addToPropertys(property381)
            da.addToPropertys(property391)
            da.addToPropertys(property301)
            da.addToPropertys(property312)
            da.addToPropertys(property322)
            da.addToPropertys(property332)
            da.addToPropertys(property342)
            da.addToPropertys(property352)
            da.addToPropertys(property362)
            da.addToPropertys(property372)
            da.addToPropertys(property382)

            //operation
            da.addToOperations(operation1)
            da.addToOperations(operation2)
            da.addToOperations(operation3)
            da.addToOperations(operation4)

            //菜单权限数据
            print("init meuns...")
            Menus menus1 = new Menus(name: 'jjgl', title: '基金管理', url: '').save(failOnError: true)
            Menus menus11 = new Menus(name: 'jjxx', title: '基金信息', url: 'fund-list.jsp', parentId: menus1.id).save(failOnError: true)
            Menus menus12 = new Menus(name: 'xzjj', title: '新增基金', url: 'fund-create.jsp', parentId: menus1.id).save(failOnError: true)
            Menus menus13 = new Menus(name: 'htdj', title: '合同登记', url: 'hetong-dengji.jsp', parentId: menus1.id).save(failOnError: true)
            Menus menus14 = new Menus(name: 'htly', title: '合同领用', url: 'hetong-lingyong.jsp', parentId: menus1.id).save(failOnError: true)
            Menus menus15 = new Menus(name: 'htgh', title: '合同归还', url: 'hetong-guihuan.jsp', parentId: menus1.id).save(failOnError: true)

            Menus menus2 = new Menus(name: 'khgl', title: '客户管理', url: '').save(failOnError: true)
            Menus menus21 = new Menus(name: 'tjtzda', title: '添加投资档案', url: 'investment.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus22 = new Menus(name: 'dytzqrs', title: '打印投资确认书', url: 'investment-print.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus24 = new Menus(name: 'ztdagl', title: '投资档案管理', url: 'invest-list.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus25 = new Menus(name: 'htcx', title: '合同查询', url: 'hetong-query.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus26 = new Menus(name: 'tccx', title: '提成查询', url: 'commission_query.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus27 = new Menus(name: 'tcsq', title: '提成申请', url: 'commission_apply.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus28 = new Menus(name: 'dfcx', title: '兑付查询', url: 'cash_app.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus29 = new Menus(name: 'dfsq', title: '兑付申请', url: 'cash_list.jsp', parentId: menus2.id).save(failOnError: true)
            Menus menus20 = new Menus(name: 'czjlrz', title: '操作记录日志', url: 'czjlrz-list.jsp', parentId: menus2.id).save(failOnError: true)

            Menus menus3 = new Menus(name: 'tssq', title: '特殊申请', url: '').save(failOnError: true)
            Menus menus31 = new Menus(name: 'hzxx', title: '汇总信息', url: 'specialSummary.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus32 = new Menus(name: 'tssqgl', title: '特殊申请管理', url: 'special-treat-list.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus33 = new Menus(name: 'wtfksq', title: '委托付款申请', url: 'special-paytreat.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus34 = new Menus(name: 'dqztsq', title: '到期转投申请', url: 'special_treat.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus35 = new Menus(name: 'wdqztsq', title: '未到期转投申请', url: 'special_untreat.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus36 = new Menus(name: 'thsq', title: '退伙申请', url: 'refund_add.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus37 = new Menus(name: 'xtsq', title: '续投申请', url: 'continuedinvestment-add.jsp', parentId: menus3.id).save(failOnError: true)
            Menus menus38 = new Menus(name: 'hbsq', title: '合并申请', url: 'special-merge.jsp', parentId: menus3.id).save(failOnError: true)

            Menus menus4 = new Menus(name: 'xmgl', title: '项目管理', url: '').save(failOnError: true)
            Menus menus41 = new Menus(name: 'xjxm', title: '新建项目', url: 'new_project.jsp', parentId: menus4.id).save(failOnError: true)
            Menus menus42 = new Menus(name: 'xmlb', title: '项目列表', url: 'project_list.jsp', parentId: menus4.id).save(failOnError: true)
            Menus menus43 = new Menus(name: 'xzhkjl', title: '新增汇款记录', url: 'new_pay_record.jsp', parentId: menus4.id).save(failOnError: true)
            Menus menus44 = new Menus(name: 'xzskjl', title: '新增收款记录', url: 'new_receive_record.jsp', parentId: menus4.id).save(failOnError: true)
            Menus menus45 = new Menus(name: 'xmzkmxb', title: '项目转款明细表', url: 'pay_record_list.jsp', parentId: menus4.id).save(failOnError: true)
            Menus menus46 = new Menus(name: 'xmjs', title: '项目结算', url: 'end_project.jsp', parentId: menus4.id).save(failOnError: true)

            Menus menus5 = new Menus(name: 'dagl', title: '档案管理', url: '').save(failOnError: true)
            Menus menus51 = new Menus(name: 'jycx', title: '借阅/查询', url: 'filepackage-list.jsp', parentId: menus5.id).save(failOnError: true)
            Menus menus52 = new Menus(name: 'dark', title: '档案入库', url: 'filepackage-add.jsp', parentId: menus5.id).save(failOnError: true)

            Menus menus6 = new Menus(name: 'yhyw', title: '银行业务', url: '').save(failOnError: true)
            Menus menus61 = new Menus(name: 'fkdcx', title: '付款单查询', url: 'bankingpaymentorder.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus62 = new Menus(name: 'jzpzscsz', title: '记账凭证生成设置', url: 'bankordersetting.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus63 = new Menus(name: 'jzpzcx', title: '记账凭证查询', url: 'bankorder.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus64 = new Menus(name: 'yhxxcx', title: '银行信息查询', url: 'bankingTransportation.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus65 = new Menus(name: 'jyxxcx', title: '交易信息查询', url: 'bankingTransportation.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus66 = new Menus(name: 'tkcx', title: '退款查询', url: 'bankingTransportation.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus67 = new Menus(name: 'dzhdgl', title: '电子回单管理', url: 'bankingTransportation.jsp', parentId: menus6.id).save(failOnError: true)
            Menus menus68 = new Menus(name: 'yhywxtrz', title: '银行业务系统日志', url: 'bankingTransportation.jsp', parentId: menus6.id).save(failOnError: true)

            Menus menus7 = new Menus(name: 'xtsz', title: '系统设置', url: '').save(failOnError: true)
            Menus menus71 = new Menus(name: 'qxgl', title: '权限管理', url: 'authority-list.jsp', parentId: menus7.id).save(failOnError: true)
            Menus menus72 = new Menus(name: 'gsgl', title: '公司管理(有限合伙)', url: 'company-list.jsp', parentId: menus7.id).save(failOnError: true)
//            Menus menus73 = new Menus(name: 'xzgs', title: '新增公司', url: 'company-create.jsp', parentId: menus7.id).save(failOnError: true)
            Menus menus74 = new Menus(name: 'bmgl', title: '部门管理', url: 'department-list.jsp', parentId: menus7.id).save(failOnError: true)
//            Menus menus75 = new Menus(name: 'xzbm', title: '新增部门', url: 'department-create.jsp', parentId: menus7.id).save(failOnError: true)
            Menus menus76 = new Menus(name: 'jsgl', title: '角色管理', url: 'role-list.jsp', parentId: menus7.id).save(failOnError: true)
//            Menus menus77 = new Menus(name: 'xzjs', title: '新增角色', url: 'role-create.jsp', parentId: menus7.id).save(failOnError: true)
            Menus menus78 = new Menus(name: 'yhgl', title: '用户管理', url: 'user-list.jsp', parentId: menus7.id).save(failOnError: true)
            Menus menus23 = new Menus(name: 'khxxxg', title: '客户(项目方)信息修改', url: 'customer-list.jsp', parentId: menus7.id).save(failOnError: true)
            Menus menus79 = new Menus(name: 'xmglsetting', title: '项目管理', url: 'project-model-setting.jsp', parentId: menus7.id).save(failOnError: true)

            //权限关系
            new MenusRole(menus: menus1, role: managerRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus11, role: managerRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus12, role: managerRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus13, role: managerRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus14, role: managerRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus15, role: managerRole, visible: true).save(failOnError: true)
            //管理员为所有权限
            new MenusRole(menus: menus1, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus11, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus12, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus13, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus14, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus15, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus2, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus21, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus22, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus23, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus24, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus25, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus26, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus27, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus28, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus29, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus20, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus3, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus31, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus32, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus33, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus34, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus35, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus36, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus37, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus38, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus4, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus41, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus42, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus43, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus44, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus45, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus46, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus5, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus51, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus52, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus6, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus61, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus62, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus63, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus64, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus65, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus66, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus67, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus68, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus7, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus71, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus72, role: adminRole, visible: true).save(failOnError: true)
//            new MenusRole(menus: menus73, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus74, role: adminRole, visible: true).save(failOnError: true)
//            new MenusRole(menus: menus75, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus76, role: adminRole, visible: true).save(failOnError: true)
//            new MenusRole(menus: menus77, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus78, role: adminRole, visible: true).save(failOnError: true)
            new MenusRole(menus: menus79, role: adminRole, visible: true).save(failOnError: true)


            print("init meuns end")

            //基金相关url与对应操作关系
            new OperationsAPI(resoureClass: Fund.class.toString(), url: '/api/fund', method: 'PUT', czlx: 'creat').save(failOnError: true)
            new OperationsAPI(resoureClass: Fund.class.toString(), url: '/api/fund/update', method: 'PUT', czlx: 'update').save(failOnError: true)
            new OperationsAPI(resoureClass: Fund.class.toString(), url: '/api/fund/mainPage', method: 'POST', czlx: 'read').save(failOnError: true)
            new OperationsAPI(resoureClass: Fund.class.toString(), url: '/api/fund', method: 'GET', czlx: 'read').save(failOnError: true)
            //档案相关url
            new OperationsAPI(resoureClass: InvestmentArchives.class.toString(), url: '/api/investmentArchives/CreateOrUpdate', method: 'PUT', czlx: 'creat').save(failOnError: true)
            new OperationsAPI(resoureClass: InvestmentArchives.class.toString(), url: '/api/investmentArchives/CreateOrUpdate', method: 'PUT', czlx: 'update').save(failOnError: true)
            new OperationsAPI(resoureClass: InvestmentArchives.class.toString(), url: '/api/investmentArchives/readAllForPage', method: 'POST', czlx: 'read').save(failOnError: true)
            new OperationsAPI(resoureClass: InvestmentArchives.class.toString(), url: '/api/investmentArchives/nameLike', method: 'GET', czlx: 'read').save(failOnError: true)
            //客户相关url
            new OperationsAPI(resoureClass: CustomerArchives.class.toString(), url: '/api/customerArchives/nameLike', method: 'GET', czlx: 'read').save(failOnError: true)
            new OperationsAPI(resoureClass: CustomerArchives.class.toString(), url: '/api/customerArchives', method: 'PUT', czlx: 'creat').save(failOnError: true)
            new OperationsAPI(resoureClass: CustomerArchives.class.toString(), url: '/api/customerArchives/readAllForPage', method: 'GET', czlx: 'read').save(failOnError: true)
            new OperationsAPI(resoureClass: CustomerArchives.class.toString(), url: '/api/customerArchives/update', method: 'POST', czlx: 'update').save(failOnError: true)
            new OperationsAPI(resoureClass: CustomerArchives.class.toString(), url: '/api/customerArchives/getcustomer', method: 'GET', czlx: 'read').save(failOnError: true)

    }

    def destroy = {


    }

    def init_metaExtend = {
        print("set Date out format 'yyyy/MM/dd HH:mm:ss'")
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy/MM/dd HH:mm:ss")
        }

        println "init_metaExtend start"
        Object.metaClass.union = { obj ->
            obj.properties.each { prop, val ->
                if (prop in ["metaClass", "class"]) return
                if (delegate.delegate.hasProperty(prop) && val) {
                    if (delegate.delegate.hasProperty(prop).setter) {
                        delegate.delegate[prop] = val
                    } else {

                    }

                }
            }
        }

        Object.metaClass.unionMap = { map ->
            map.each {
                if (delegate.delegate.hasProperty(it.key)) {
                    if (delegate.delegate.hasProperty(it.key).setter) {
                        delegate.delegate[it.key] = it.value
                    } else {

                    }
                }
            }
        }
    }

    def initFlowModel = {


        def tsWorkflowModel = TSWorkflowModel.findByModelName("projectCreateFlow")
        if (tsWorkflowModel) {
            println "flow model exist!"
            return;
        }

        def admin = User.findByUsername('admin')


        TSWorkflowModel tsWorkflow = new TSWorkflowModel(modelName: "projectCreateFlow");
        tsWorkflow.save(failOnError: true)

        def financialIncharger = Role.findByAuthority("FinancialIncharger")
        def projectIncharger = Role.findByAuthority("ProjectIncharger") ?: new Role(authority: "ProjectIncharger", name: "项目部负责人").save(failOnError: true);
        def ministryIncharger = Role.findByAuthority("MinistryIncharger")

        TSWorkflowModelPhase phase1 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 1, phaseEn: "gatherInfo", phaseName: "步骤1.1：资料采集（项目部负责并填写）");
        phase1.addToPhaseParticipants(projectIncharger)
        phase1.save(failOnError: true)
        TSWorkflowModelPhase phase2 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 2, phaseEn: "gatherOA", phaseName: "步骤1.2：资料评判——OA审核");
//        phase2.addToPhaseParticipants(projectIncharger)
        phase2.save(failOnError: true)
        TSWorkflowModelPhase phase3 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 3, phaseEn: "research", phaseName: "步骤1.3：现场考察（方案确定）（项目部负责发起申请，法务部，财务部配合）");
        phase3.addToPhaseParticipants(projectIncharger)
        phase3.save(failOnError: true)
        TSWorkflowModelPhase phase4 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 4, phaseEn: "researchOA", phaseName: "步骤1.4：现场考察——OA审核");
//        phase4.addToPhaseParticipants(projectIncharger)
        phase4.save(failOnError: true)
        TSWorkflowModelPhase phase5 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 5, phaseEn: "meeting", phaseName: "步骤1.5：投决会（项目部负责发起申请，法务部，财务部配合）");
        phase5.addToPhaseParticipants(projectIncharger)
        phase5.save(failOnError: true)
        TSWorkflowModelPhase phase6 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 6, phaseEn: "otherEA", phaseName: "步骤1.6：第三方法律机构（项目部负责发起申请，法务部，财务部配合）");
        phase6.addToPhaseParticipants(projectIncharger)
        phase6.save(failOnError: true)
        TSWorkflowModelPhase phase9 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 7, phaseEn: "makeContact", phaseName: "步骤2：项目合同——选择预发行基金以及录入合同资料（项目部负责发起申请，法务部，财务部配合）");
        phase9.addToPhaseParticipants(projectIncharger)
        phase9.save(failOnError: true)
        TSWorkflowModelPhase phase10 = new TSWorkflowModelPhase(phaseModel: tsWorkflow, phaseIndex: 8, phaseEn: "makeContactOA", phaseName: "步骤2.1：项目合同——OA审核");
        phase10.addToPhaseParticipants(projectIncharger)
        phase10.save(failOnError: true)

        tsWorkflow.addToModelPhases(phase1)
        tsWorkflow.addToModelPhases(phase2)
        tsWorkflow.addToModelPhases(phase3)
        tsWorkflow.addToModelPhases(phase4)
        tsWorkflow.addToModelPhases(phase5)
        tsWorkflow.addToModelPhases(phase6)
//        tsWorkflow.addToModelPhases(phase8)
        tsWorkflow.addToModelPhases(phase9)
        tsWorkflow.addToModelPhases(phase10)
        tsWorkflow.save(failOnError: true)

        //特殊时间允许通过
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date fromDate = dateFormat.parse("20150101");
        Date toDate = dateFormat.parse("20250101");
        SpecailAccess sa1 = new SpecailAccess(fromDate: fromDate, toDate: toDate, accessor: 1, projectId: 1);
        sa1.save(failOnError: true)
        SpecailAccess sa2 = new SpecailAccess(fromDate: fromDate, toDate: toDate, accessor: 2, projectId: 2, phaseEn: "gatherInfoBean");
        sa2.save(failOnError: true)

        //能够在历史步骤中，继续提交节点的权限，这里面的提交不影响流程前进
        Role.findByAuthority("ProjectHistoryModifier") ?: new Role(authority: "ProjectHistoryModifier", name: "项目历史节点提交控制者").save(failOnError: true);

    }
}
