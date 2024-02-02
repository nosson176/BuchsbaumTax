package com.sifradigital.framework.auth;

import java.security.Principal;

public interface Authenticator<C, P extends Principal> {

    P authenticate(C credentials);
}
