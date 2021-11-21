package com.buchsbaumtax.app.config;

import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.CORSFilter;
import com.sifradigital.framework.SifraApplication;
import com.sifradigital.framework.auth.AuthDynamicFeature;
import com.sifradigital.framework.auth.AuthenticatedProvider;
import com.sifradigital.framework.auth.SessionAuthenticationFilter;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.db.DatabaseConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class BuchsbaumApplication extends SifraApplication {

    public BuchsbaumApplication() {
        packages(true, "com.buchsbaumtax.app.resource");

        register(new CORSFilter());

        DatabaseConfig.Connection connection = new DatabaseConfig.Connection("jdbc/BTDB", "jdbc:postgresql://localhost:5432/buchsbaum", "postgres", "sifra123");
        DatabaseConfig databaseConfig = new DatabaseConfig(connection, "com.buchsbaumtax.core.dao", false, false);
        Database.init(databaseConfig);

        register(RolesAllowedDynamicFeature.class);
        register(new AuthDynamicFeature(new SessionAuthenticationFilter<>(s -> Database.dao(UserDAO.class).getByToken(s), new UserAuthorizer())));
        register(new AuthenticatedProvider.Binder<>(User.class));
    }
}
