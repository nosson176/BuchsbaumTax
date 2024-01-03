package com.sifradigital.framework.auth;

import java.security.Principal;

public class AuthorizeAllAuthorizer<P extends Principal> implements Authorizer<P> {

    @Override
    public boolean authorize(P principal, String role) {
        return true;
    }
}
