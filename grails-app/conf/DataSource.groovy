dataSource {
    pooled = true
    jmxExport = true

//    driverClassName = "org.h2.Driver"
//    username = "sa"
//    password = ""

    driverClassName = "com.mysql.jdbc.Driver"
//    username = "jsy"
//    password = "jsy1qaz"
    username = "root"
    password = "123654"
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect

}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    development {
        dataSource {
//            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            dbCreate = "update"
//            url = "jdbc:mysql://192.168.1.59:3306/jsytest?user=jsy&password=jsy1qaz&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
//            url = "jdbc:mysql://192.168.4.141:3306/test1?user=root&password=surewin&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
            url = "jdbc:mysql://localhost:3306/jsytest?user=root&password=123654&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
            properties {
                // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
                jmxEnabled = true
                initialSize = 5
                maxActive = 50
                minIdle = 5
                maxIdle = 25
                maxWait = 10000
                maxAge = 10 * 60000
                timeBetweenEvictionRunsMillis = 5000
                minEvictableIdleTimeMillis = 60000
                validationQuery = "SELECT 1"
                validationQueryTimeout = 3
                validationInterval = 15000
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                jdbcInterceptors = "ConnectionState"
                defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
//            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"

            dbCreate = "update"
//            url = "jdbc:mysql://localhost:3306/jsytest?user=root&password=1234&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
            url = "jdbc:mysql://192.168.1.59:3306/jsytest?user=jsy&password=jsy1qaz&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false"
            properties {
                // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
                jmxEnabled = true
                initialSize = 5
                maxActive = 50
                minIdle = 5
                maxIdle = 25
                maxWait = 10000
                maxAge = 10 * 60000
                timeBetweenEvictionRunsMillis = 5000
                minEvictableIdleTimeMillis = 60000
                validationQuery = "SELECT 1"
                validationQueryTimeout = 3
                validationInterval = 15000
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                jdbcInterceptors = "ConnectionState"
                defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
