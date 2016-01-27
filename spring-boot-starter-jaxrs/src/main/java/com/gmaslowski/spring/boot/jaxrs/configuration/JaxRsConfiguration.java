package com.gmaslowski.spring.boot.jaxrs.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.gmaslowski.spring.boot.jaxrs.stereotype.annotation.JaxRsController;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class JaxRsConfiguration {

    @Autowired
    private ApplicationContext ctx;

    @Value("${jaxrs.path:/*}")
    private String cxfPath;

    @Value("${jaxrs.log.requests:false}")
    private boolean logRequests;

    @Bean
    public ServletRegistrationBean cxfServletRegistrationBean() {
        return new ServletRegistrationBean(new CXFServlet(), cxfPath);
    }

    @Bean
    public Server jaxRsServer() {
        List<Object> jaxRsControllers = findJaxRsControllers();
        List<Object> jaxRsProviders = findJaxRsProviders();

        return createJaxRsServer(jaxRsControllers, jaxRsProviders);
    }

    private Server createJaxRsServer(List<Object> jaxRsControllers, List<Object> jaxRsProviders) {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(ctx.getBean(SpringBus.class));
        factory.setAddress("/");
        factory.setServiceBeans(jaxRsControllers);
        factory.setProviders(jaxRsProviders);

        if (logRequests) {
            factory.getInInterceptors().add(new LoggingInInterceptor());
        }

        return factory.create();
    }

    private List<Object> findJaxRsProviders() {
        return new ArrayList<Object>(ctx.getBeansWithAnnotation(Provider.class).values());
    }

    private List<Object> findJaxRsControllers() {
        return new ArrayList<Object>(ctx.getBeansWithAnnotation(JaxRsController.class).values());
    }

    @Bean
    @ConditionalOnMissingBean
    public JacksonJsonProvider jsonProvider(ObjectMapper objectMapper) {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(objectMapper);
        return provider;
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
