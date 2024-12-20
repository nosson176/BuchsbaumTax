package com.buchsbaumtax.app.job;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private static final String SMTP_HOST = "smtp.gmail.com"; // החלף בכתובת ה-SMTP שלך
    private static final String SMTP_PORT = "587"; // לדוגמה, לפורט 587 או 465
    private static final String SMTP_USER = "elyasaf124@gmail.com"; // כתובת האימייל שלך
    private static final String SMTP_PASSWORD = "vsaa bjlz mohl jahi "; // סיסמת האימייל שלך

    public void sendEmail(String to, String subject, String body) {
        // לוגיקה לשליחת דוא"ל
        try {
            // הגדרת פרטי החיבור לשרת ה-SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.host", SMTP_HOST);
            properties.put("mail.smtp.port", SMTP_PORT);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // יצירת Session עם פרטי ההתחברות
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                }
            });

            // יצירת המייל
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // שליחת המייל
            Transport.send(message);

            LOGGER.info("Email sent successfully to: " + to);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending email to: " + to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
