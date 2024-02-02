package com.sifradigital.framework.auth;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.Principal;
import java.util.function.Function;

@Singleton
public class AuthenticatedProvider<T extends Principal> extends AbstractValueParamProvider {

    private final Class<T> principalClass;

    @Inject
    public AuthenticatedProvider(MultivaluedParameterExtractorProvider mpep, PrincipalClassProvider<T> principalClassProvider) {
        super(() -> mpep, Parameter.Source.UNKNOWN);
        this.principalClass = principalClassProvider.clazz;
    }

    @Nullable
    @Override
    protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {
        if (!parameter.isAnnotationPresent(Authenticated.class)) {
            return null;
        }
        else if (principalClass.equals(parameter.getRawType())) {
            return request -> request.getSecurityContext().getUserPrincipal();
        }
        else {
            return null;
        }
    }

    @Singleton
    static class PrincipalClassProvider<T extends Principal> {

        private final Class<T> clazz;

        PrincipalClassProvider(Class<T> clazz) {
            this.clazz = clazz;
        }
    }

    public static class Binder<T extends Principal> extends AbstractBinder {

        private final Class<T> principalClass;

        public Binder(Class<T> principalClass) {
            this.principalClass = principalClass;
        }

        @Override
        protected void configure() {
            bind(new PrincipalClassProvider<>(principalClass)).to(PrincipalClassProvider.class);
            bind(AuthenticatedProvider.class).to(ValueParamProvider.class).in(Singleton.class);
        }
    }
}