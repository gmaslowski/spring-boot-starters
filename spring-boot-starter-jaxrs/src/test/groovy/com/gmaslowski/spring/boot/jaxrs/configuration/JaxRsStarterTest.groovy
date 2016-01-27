package com.gmaslowski.spring.boot.jaxrs.configuration

import groovyx.net.http.RESTClient
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = JaxRsCxfApplicationContext.class)
class JaxRsStarterTest extends Specification {

    def client = new RESTClient("http://localhost:8080")

    def "should verify get"() {
        when:
        def resp = client.get(path: "/api/f1/constructors/mclaren/circuits/monza/drivers/alonso.json")

        then:
        resp.data.id == 7
    }

}