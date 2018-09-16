package com.bzawadzki.lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface WireMockTest {

  int port() default 8181;
  boolean cleanStubs() default true;
  boolean restart() default true;
  boolean useTempFolders() default true;
}
