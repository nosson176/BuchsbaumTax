package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.user.Login;
import com.buchsbaumtax.app.domain.user.UserCRUD;
import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.app.dto.Credentials;
import com.buchsbaumtax.app.dto.PasswordResetRequest;
import com.buchsbaumtax.app.dto.Token;
import com.buchsbaumtax.app.job.TokenValidationResult;
import com.buchsbaumtax.app.job.UserService;
import com.buchsbaumtax.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/sessions")
public class SessionResource {
    private static final Logger logger = LoggerFactory.getLogger(SessionResource.class);

    @POST
    public Token login(Credentials credentials){ return new Login().login(credentials);}

    @POST
    @Path("/forgot-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public BaseResponse forgotPassword(ForgotPasswordRequest emailRequest) {
        logger.info("Received email: {}", emailRequest.getEmail());
        if (emailRequest.getEmail() == null) {
            return new BaseResponse(false, "Email is required");
        }

        String email = emailRequest.getEmail().toLowerCase(); // Convert email to lowercase
        return new UserCRUD().validateEmail(email);
    }

    // פונקציה לשחזור סיסמה באמצעות טוקן
    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse resetPassword(PasswordResetRequest request) {
        logger.info("Received reset request with token: {}", request.getToken());

        // בדיקה אם הטוקן בתוקף
        TokenValidationResult isValidToken = new UserService().validateToken(request.getToken().getToken());
        logger.info("111: {}", isValidToken);
        if (!isValidToken.isValid()) {
        logger.info("falseee: {}", isValidToken);
            return new BaseResponse(false, "Invalid or expired token");
        }
        String email =isValidToken.getToken().getEmail();
        logger.info("trueee: {}", email);
        if (email == null) {
            return new BaseResponse(false, "Token does not contain a valid email");
        }


//        // Step 3: Find the user by email
        User user = new UserCRUD().getUserByEmail(email);
        logger.info("userrr: {}", user);
        if (user == null) {
            return new BaseResponse(false, "User not found with the provided email");
        }
        BaseResponse isUpdate = new UserCRUD().updatePassword2(user.getId(), request);
        logger.info("isUpdate: {}", isUpdate);
        String is = isUpdate.getSuccess();
        if(!is.equals("Success")){
        return new BaseResponse(false, "Password updated faild");
        }
        return new BaseResponse(true, "Password updated successfully");
        // עדכון הסיסמה
//        boolean isPasswordUpdated = passwordResetService.updatePassword(request.getToken(), request.getPassword());
//        if (isPasswordUpdated) {
//            return new BaseResponse(true, "Password updated successfully");
//        } else {
//            return new BaseResponse(false, "Error updating password");
//        }
    }


}

