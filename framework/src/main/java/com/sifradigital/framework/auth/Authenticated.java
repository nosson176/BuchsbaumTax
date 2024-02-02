package com.sifradigital.framework.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.security.Principal;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, METHOD, PARAMETER})
public @interface Authenticated {
    Class<? extends Principal> value() default Principal.class;
}