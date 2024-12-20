package com.buchsbaumtax.app.job;

import com.buchsbaumtax.core.dao.ResetPasswordTokenDAO;
import com.sifradigital.framework.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.logging.Level;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void sendPasswordResetEmail(String email) {
        try {
            // Generate a unique reset token
            String token = generateResetToken();

            // Create reset link based on environment
            String resetLink = createResetLink(token);

            // Create password reset token with 1-hour expiration
            OffsetDateTime expiresAt = OffsetDateTime.now().plusHours(1);

            // Save token to database
            Database.dao(ResetPasswordTokenDAO.class).create(
                    email,
                    token,
                    OffsetDateTime.now(),
                    expiresAt
            );

            // Send email
            sendPasswordResetEmailToUser(email, resetLink);

            logger.info("Password reset email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Error sending password reset email to: {}", email, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private String createResetLink(String token) {
        String mode = System.getenv("APP_MODE");
        return ("production".equalsIgnoreCase(mode))
                ? "https://buch-tax/resetPassword?token=" + token
                : "http://localhost:3000/resetPassword?token=" + token;
    }

    private void sendPasswordResetEmailToUser(String email, String resetLink) {
        String subject = "שחזור סיסמה";
        String body = "לשחזור סיסמה לחץ על הקישור הבא: " + resetLink;

        // Assuming emailService is initialized elsewhere or injected
        new EmailService().sendEmail(email, subject, body);
    }

    public TokenValidationResult validateToken(String token) {
        PasswordResetToken resetToken = Database.dao(ResetPasswordTokenDAO.class).findValidToken(token);

        if (resetToken == null) {
            logger.info("Token not found or invalid: {}", token);
            return new TokenValidationResult(false, null);
        }

        return new TokenValidationResult(true, resetToken);
    }
}