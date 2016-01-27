package com.gmaslowski.spring.boot.jaxrs.configuration

import com.gmaslowski.spring.boot.jaxrs.starter.EnableJaxRs
import com.gmaslowski.spring.boot.jaxrs.stereotype.annotation.JaxRsController
import org.apache.cxf.jaxrs.ext.PATCH
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.context.annotation.ComponentScan

import javax.servlet.http.HttpServletResponse
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.HEAD
import javax.ws.rs.OPTIONS
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context

import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN

@SpringApplicationConfiguration
@ComponentScan("com.gmaslowski.spring.boot.jaxrs")
@EnableJaxRs
@EnableAutoConfiguration
class JaxRsCxfApplicationContext {

    @JaxRsController
    class JaxRsCxfRestController implements JaxRsRestController {

        def JaxRsCxfRestController() {}

        @Context
        HttpServletResponse response

        @Override
        RestResponse post(RestRequest restRequest) {
            responseFrom(restRequest)
        }

        @Override
        List<RestResponse> get() {
            [createRestResponse(), createRestResponse(), createRestResponse()]
        }

        @Override
        RestResponse get(@PathParam("id") String id) {
            createRestResponse(id)
        }

        private RestResponse createRestResponse() {
            createRestResponse("1")
        }

        private RestResponse createRestResponse(String id) {
            def response = new RestResponse()
            response.id = id
            response.description = "Description"

            response
        }

        @Override
        RestResponse delete(RestRequest restRequest) {
            response.setStatus(SC_ACCEPTED)

            responseFrom(restRequest)
        }

        @Override
        RestResponse put(RestRequest restRequest) {
            responseFrom(restRequest)
        }

        private RestResponse responseFrom(RestRequest restRequest) {
            def response = new RestResponse()
            response.id = restRequest.id
            response.description = restRequest.description

            response
        }


        @Override
        Map<String, String> options(RestRequest restRequest) {
            ["key1": "value1", "key2": "value2", "key3": "value3"]
        }

        @Override
        void head(RestRequest restRequest) {
            response.setStatus(SC_FORBIDDEN)
        }

        @Override
        RestResponse patch(Map<String, String> changes) {
            createRestResponse(changes.getOrDefault("id", "777"))
        }
    }

    @Path("/rest")
    @Produces("application/json")
    public interface JaxRsRestController {

        @POST
        public RestResponse post(RestRequest restRequest)

        @GET
        public List<RestResponse> get()

        @GET
        @Path("/{id}")
        public RestResponse get(@PathParam("id") String id)

        @DELETE
        public RestResponse delete(RestRequest restRequest)

        @PUT
        public RestResponse put(RestRequest restRequest)

        @OPTIONS
        public Map<String, String> options(RestRequest restRequest)

        @HEAD
        public void head(RestRequest restRequest)

        @PATCH
        public RestResponse patch(Map<String, String> changes)
    }

    class RestResponse {
        def id
        def description
    }

    class RestRequest {
        def id
        def description
    }
}
