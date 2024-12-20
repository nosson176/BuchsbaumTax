package com.buchsbaumtax.core.dao;

import com.buchsbaumtax.app.job.PasswordResetToken;
import com.sifradigital.framework.db.Dao;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.OffsetDateTime;

@Dao
public interface ResetPasswordTokenDAO {
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO password_reset_tokens (email, token, created_at, expires_at) " +
            "VALUES (:email, :token, :createdAt, :expiresAt)")
    Long create(
            @Bind("email") String email,
            @Bind("token") String token,
            @Bind("createdAt") OffsetDateTime createdAt,
            @Bind("expiresAt") OffsetDateTime expiresAt
    );

    @RegisterFieldMapper(PasswordResetToken.class)
    @SqlQuery("SELECT * FROM password_reset_tokens WHERE token = :token AND used = false AND expires_at > NOW()")
    PasswordResetToken findValidToken(@Bind("token") String token);

    @SqlUpdate("UPDATE password_reset_tokens SET used = true WHERE token = :token")
    void markTokenAsUsed(@Bind("token") String token);

    @SqlUpdate("DELETE FROM password_reset_tokens WHERE expires_at < NOW()")
    void deleteExpiredTokens();
}