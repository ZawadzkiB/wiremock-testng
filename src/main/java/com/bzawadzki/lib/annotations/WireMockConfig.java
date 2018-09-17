package com.bzawadzki.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface WireMockConfig {

  int port() default 8181;
  boolean useTempFolder() default true;
  boolean cleanStubsAfterTest() default false;
}
