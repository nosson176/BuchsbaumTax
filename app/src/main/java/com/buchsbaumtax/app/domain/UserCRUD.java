package com.buchsbaumtax.app.domain;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.UpdatePasswordRequest;
import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.util.PasswordUtils;
import com.sifradigital.framework.validation.Validator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class UserCRUD {
    public List<User> getAll() {
        return Database.dao(UserDAO.class).getAll();
    }

    public User create(User user) {
        new Validator()
                .required(user.getUsername())
                .required(user.getUserType())
                .minLength(user.getPassword(), 6, "Password is too short")
                .validateAndGuard();

        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        int id = Database.dao(UserDAO.class).create(user);
        return Database.dao(UserDAO.class).get(id);

    }

    public User update(int userId, User user) {
        if (user.getId() != userId) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        new Validator()
                .required(user.getUsername())
                .required(user.getUserType())
                .validateAndGuard();

        Database.dao(UserDAO.class).update(user);
        return Database.dao(UserDAO.class).get(userId);
    }

    public BaseResponse delete(int userId) {
        Database.dao(UserDAO.class).delete(userId);
        return new BaseResponse(true);
    }

    public BaseResponse updatePassword(int userId, UpdatePasswordRequest updatePasswordRequest) {
        User user = Database.dao(UserDAO.class).get(userId);
        if (user == null) {
            return new BaseResponse(false);
        }

        new Validator()
                .minLength(updatePasswordRequest.getNewPassword(), 6, "Password is too short")
                .validateAndGuard();

        String hashPassword = PasswordUtils.hashPassword(updatePasswordRequest.getNewPassword());
        Database.dao(UserDAO.class).updatePassword(userId, hashPassword);
        return new BaseResponse(true);
    }
}
