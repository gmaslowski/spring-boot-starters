package com.gmaslowski.spring.boot.jaxrs.configuration;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(JaxRsConfiguration.class)
public @interface EnableJaxRsConfiguration {
}
