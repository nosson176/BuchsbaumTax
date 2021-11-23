package com.buchsbaumtax.app.config;

import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.auth.Authorizer;

public class UserAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getUserType().equals(role);
    }
}
