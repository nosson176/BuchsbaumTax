package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.Credentials;
import com.buchsbaumtax.app.dto.Token;
import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.dao.UserPasswordDAO;
import com.buchsbaumtax.core.dao.UserSessionDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.util.GenerateToken;
import com.sifradigital.framework.util.PasswordUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Login {
    public Token login(Credentials credentials) {
        User user = Database.dao(UserDAO.class).getByEmail(credentials.getEmail());
        if (user == null) {
            throw new WebApplicationException("No user exists for that email address", Response.Status.UNAUTHORIZED);
        }

        String correctHash = Database.dao(UserPasswordDAO.class).get(user.getId());
        if (!PasswordUtils.validatePassword(credentials.getPassword(), correctHash)) {
            throw new WebApplicationException("Invalid password", Response.Status.UNAUTHORIZED);
        }

        String token = GenerateToken.generateToken();
        Database.dao(UserSessionDAO.class).create(token, user.getId());
        return new Token(token);
    }
}
