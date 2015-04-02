package com.jsy.project

import com.jsy.system.TypeConfig
import grails.transaction.Transactional

@Transactional(rollbackFor = Throwable.class)
class TestService {

    def testTransation() {
        new TypeConfig(type:1000, mapName:"testConfig", mapValue:10, description:"no desc").save(failOnError: true);
        throw new Exception("error");

    }
}
