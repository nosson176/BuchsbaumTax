package com.sifradigital.framework.auth;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Priority(Priorities.AUTHENTICATION)
public class SessionAuthenticationFilter<P extends Principal> implements ContainerRequestFilter {

    private final Authenticator<String, P> authenticator;
    private final Authorizer<P> authorizer;

    public SessionAuthenticationFilter(Authenticator<String, P> authenticator) {
        this(authenticator, new AuthorizeAllAuthorizer<>());
    }
    public SessionAuthenticationFilter(Authenticator<String, P> authenticator, Authorizer<P> authorizer) {
        this.authenticator = authenticator;
        this.authorizer = authorizer;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        token = token.substring("Bearer".length()).trim();
        final P principal = authenticator.authenticate(token);
        if (principal == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        else {
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return principal;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return authorizer.authorize(principal, role);
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            });
        }
    }
}
