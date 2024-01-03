package com.sifradigital.framework.auth;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.Principal;
import java.util.Set;
import java.util.function.Function;

public class MultiAuthenticatedProvider<T extends Principal> extends AbstractValueParamProvider {

    protected final Set<Class<? extends T>> principalClassSet;

    @Inject
    public MultiAuthenticatedProvider(MultivaluedParameterExtractorProvider mpep, PrincipalClassSetProvider<T> principalClassSetProvider) {
        super(() -> mpep, Parameter.Source.UNKNOWN);
        this.principalClassSet = principalClassSetProvider.clazzSet;
    }

    @Override
    protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {
        if (!parameter.isAnnotationPresent(Authenticated.class)) {
            return null;
        }
        else if (principalClassSet.contains(parameter.getRawType())) {
            return request -> request.getSecurityContext().getUserPrincipal();
        }
        else {
            return null;
        }
    }

    @Singleton
    protected static class PrincipalClassSetProvider<T extends Principal> {

        private final Set<Class<? extends T>> clazzSet;

        public PrincipalClassSetProvider(Set<Class<? extends T>> clazzSet) {
            this.clazzSet = clazzSet;
        }
    }

    public static class Binder<T extends Principal> extends AbstractBinder {

        private final Set<Class<? extends T>> principalClassSet;

        public Binder(Set<Class<? extends T>> principalClassSet) {
            this.principalClassSet = principalClassSet;
        }

        @Override
        protected void configure() {
            bind(new PrincipalClassSetProvider<>(principalClassSet)).to(PrincipalClassSetProvider.class);
            bind(MultiAuthenticatedProvider.class).to(ValueParamProvider.class).in(Singleton.class);
        }
    }
}
