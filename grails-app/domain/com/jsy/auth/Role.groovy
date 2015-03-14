package com.jsy.auth

class Role {

	String authority
    String name
	static mapping = {
		cache true
	}

    def beforeInsert() {
        authority=UUID.randomUUID().toString()
    }

	static constraints = {
        name blank: false,unique: true
		authority nullable: true, unique: true
	}
}
