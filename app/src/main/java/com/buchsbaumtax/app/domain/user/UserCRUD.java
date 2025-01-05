package com.buchsbaumtax.app.domain.user;

import com.buchsbaumtax.app.config.BuchsbaumApplication;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.PasswordResetRequest;
import com.buchsbaumtax.app.dto.UpdatePasswordRequest;
import com.buchsbaumtax.app.job.PasswordResetTokenRepository;
import com.buchsbaumtax.app.job.UserService;
import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.model.User;
import com.buchsbaumtax.core.model.create.UserCreate;
import com.sifradigital.framework.LoggingFilter;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.util.PasswordUtils;
import com.sifradigital.framework.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class UserCRUD {
    private static final Logger logger = LoggerFactory.getLogger(UserCRUD.class);
    public List<User> getAll() {
        return Database.dao(UserDAO.class).getAll();
    }

    public User create(UserCreate userCreate) {
        new Validator()
                .required(userCreate.getUsername())
                .required(userCreate.getUserType())
                .minLength(userCreate.getPassword(), 6, "Password is too short")
                .validateAndGuard();

        String hashedPassword = PasswordUtils.hashPassword(userCreate.getPassword());
        userCreate.setPassword(hashedPassword);
//        logger.info("user create : {}", userCreate);
        int id = Database.dao(UserDAO.class).create(userCreate);
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

    public User getUserByEmail(String email){
        logger.info("1111111111: {}", email);
         User user = Database.dao(UserDAO.class).getByEmail(email);
         return  user;
    }

    public BaseResponse validateEmail(String email) {
        logger.info("1111111111: {}", email);

        User user = Database.dao(UserDAO.class).getByEmail(email); // Query user by email
        logger.info("2222222222: {}", user);

        if (user == null) {
            logger.info("33333: {}");
            return new BaseResponse(false, "No user found with the provided email");
        }

        // יצירת מופע של UserService
        PasswordResetTokenRepository tokenRepository = new PasswordResetTokenRepository(); // ודא שאתה מספק את כל התלויות
        UserService userService = new UserService();

        // קריאה למתודה sendPasswordResetEmail
        userService.sendPasswordResetEmail(email);

        logger.info("444444444: {}", user);
        return new BaseResponse(true, "User email is valid. Reset instructions sent.");
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

    public BaseResponse updatePassword2(int userId, PasswordResetRequest updatePasswordRequest) {
        User user = Database.dao(UserDAO.class).get(userId);
        if (user == null) {
            return new BaseResponse(false);
        }

        new Validator()
                .minLength(updatePasswordRequest.getPassword(), 6, "Password is too short")
                .validateAndGuard();

        String hashPassword = PasswordUtils.hashPassword(updatePasswordRequest.getPassword());
        Database.dao(UserDAO.class).updatePassword(userId, hashPassword);
        return new BaseResponse(true);
    }
}
