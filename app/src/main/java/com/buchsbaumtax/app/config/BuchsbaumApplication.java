package com.buchsbaumtax.app.config;

import com.buchsbaumtax.core.EnvironmentProperty;
import com.buchsbaumtax.core.dao.UserDAO;
import com.buchsbaumtax.core.model.User;
import com.sifradigital.framework.CORSFilter;
import com.sifradigital.framework.LoggingFilter;
import com.sifradigital.framework.SifraApplication;
import com.sifradigital.framework.auth.AuthDynamicFeature;
import com.sifradigital.framework.auth.AuthenticatedProvider;
import com.sifradigital.framework.auth.SessionAuthenticationFilter;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.db.DatabaseConfig;
import com.sifradigital.framework.jobs.JobsConfig;
import com.sifradigital.framework.mail.MailConfig;
import com.sifradigital.framework.mail.SMS;
import com.sifradigital.framework.mail.service.TwilioSMSService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class BuchsbaumApplication extends SifraApplication {
        private static final Logger logger = LoggerFactory.getLogger(BuchsbaumApplication.class);

        public BuchsbaumApplication() {
                packages(true, "com.buchsbaumtax.app.resource");

                register(new CORSFilter());
                register(new LoggingFilter(LoggingFilter.LOG_ALL_REQUESTS));

                String mode = System.getenv("APP_MODE");
                if (mode == null) {
                        mode = "development"; // default to development if APP_MODE is not set
                        logger.info("APP_MODE is not set. Defaulting to development mode.");
                } else {
                        logger.info("APP_MODE is set to: {}", mode);
                }
                DatabaseConfig.Connection connection;
                if ("production".equalsIgnoreCase(mode)) {
                        logger.info("APP_MODE is set to: {PROD!!}", mode);
                        connection = new DatabaseConfig.Connection("jdbc/BTDB",
                                        "jdbc:postgresql://buchsbaumdb.c1w2s6uqe11n.il-central-1.rds.amazonaws.com:5432/buchsbaum",
                                        "postgres",
                                        "sifra123");
                } else {
                        logger.info("APP_MODE is set to: {DEV222!!!}", mode);
                        connection = new DatabaseConfig.Connection("jdbc/BTDB",
                                        "jdbc:postgresql://localhost:5432/buchsbaum",
                                        "postgres",
                                        "sifra123");
                }

                DatabaseConfig databaseConfig = new DatabaseConfig(connection, "com.buchsbaumtax.core.dao", false,
                                false);
                Database.init(databaseConfig);

                register(RolesAllowedDynamicFeature.class);
                register(new AuthDynamicFeature(new SessionAuthenticationFilter<>(
                                s -> Database.dao(UserDAO.class).getByToken(s), new UserAuthorizer())));
                register(new AuthenticatedProvider.Binder<>(User.class));

                JobsConfig jobsConfig = new JobsConfig("com.buchsbaumtax.app.job", 4);
                scheduleJobs(jobsConfig);

                TwilioSMSService smsService = new TwilioSMSService(
                                EnvironmentProperty.value(EnvironmentProperty.TWILIO_ACCOUNT_SID),
                                EnvironmentProperty.value(EnvironmentProperty.TWILIO_AUTH_TOKEN));
                MailConfig smsConfig = new MailConfig(smsService,
                                EnvironmentProperty.value(EnvironmentProperty.TWILIO_FROM_NUMBER));
                SMS.init(smsConfig);
        }
}
