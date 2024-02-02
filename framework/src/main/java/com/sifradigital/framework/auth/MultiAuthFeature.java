package com.sifradigital.framework.auth;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.Map;

public class MultiAuthFeature implements DynamicFeature {

    private final Map<Class<? extends Principal>, ContainerRequestFilter> authFilterMap;

    public MultiAuthFeature(Map<Class<? extends Principal>,  ContainerRequestFilter> authFilterMap) {
        this.authFilterMap = authFilterMap;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        Annotation[] classAnnotations = resourceInfo.getResourceClass().getAnnotations();
        for (Annotation annotation : classAnnotations) {
            if (annotation instanceof Authenticated && authFilterMap.containsKey(((Authenticated)annotation).value())) {
                featureContext.register(authFilterMap.get(((Authenticated)annotation).value()));
                return;
            }
        }

        Annotation[] methodAnnotations = resourceInfo.getResourceMethod().getAnnotations();
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof Authenticated && authFilterMap.containsKey(((Authenticated)annotation).value())) {
                featureContext.register(authFilterMap.get(((Authenticated)annotation).value()));
                return;
            }
        }

        final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        final Annotation[][] parameterAnnotations = am.getParameterAnnotations();
        final Class<?>[] parameterTypes = am.getParameterTypes();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Class<?> type = parameterTypes[i];
            for (final Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof Authenticated) {
                    if (authFilterMap.containsKey(((Authenticated)annotation).value())) {
                        featureContext.register(authFilterMap.get(((Authenticated)annotation).value()));
                        return;
                    }
                    else if (authFilterMap.containsKey(type)) {
                        featureContext.register(authFilterMap.get(type));
                        return;
                    }
                }
            }
        }
    }
}
