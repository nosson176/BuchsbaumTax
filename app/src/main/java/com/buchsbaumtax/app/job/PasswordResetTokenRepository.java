package com.buchsbaumtax.app.job;

public class PasswordResetTokenRepository {

    public void save(PasswordResetToken token) {
        // כאן תוסיף את הלוגיקה לשמור את הטוקן בבסיס נתונים או כל מקום אחר
        System.out.println("Token saved for email: " + token);

    }
}
