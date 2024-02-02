package com.sifradigital.framework.auth;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import java.lang.annotation.Annotation;

public class AuthDynamicFeature implements DynamicFeature {

    private final ContainerRequestFilter authFilter;

    public AuthDynamicFeature(ContainerRequestFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        Annotation[] classAnnotations = resourceInfo.getResourceClass().getAnnotations();
        for (Annotation annotation : classAnnotations) {
            if (annotation instanceof Authenticated) {
                featureContext.register(authFilter);
                return;
            }
        }

        Annotation[] methodAnnotations = resourceInfo.getResourceMethod().getAnnotations();
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof Authenticated) {
                featureContext.register(authFilter);
                return;
            }
        }

        final Annotation[][] parameterAnnotations =  resourceInfo.getResourceMethod().getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (final Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Authenticated) {
                    featureContext.register(authFilter);
                    return;
                }
            }
        }
    }
}
