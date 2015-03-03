package com.jsy.auth

import com.jsy.system.Department

class User {

	transient springSecurityService

	String username
	String password
    //中文名
    String chainName
    //收款人
    String skr=""
    //开户行
    String khh
    //银行账号
    String yhzh

    //用户类型 公司还是个人
    boolean  isUser=true

    //部门
    Department department

	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false//, unique: true
		password blank: false
        chainName nullable: true
        khh nullable: true
        yhzh nullable: true
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
