package com.sifradigital.framework.db;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.postgresql.ds.PGSimpleDataSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Set;

public class Database {

    private static final Logger Log = LoggerFactory.getLogger(Database.class);

    private static final ClassToInstanceMap<Object> map = MutableClassToInstanceMap.create();

    public static void init(DatabaseConfig config) {
        DataSource dataSource = lookupDataSource(config.getConnection());

        // Configure Jdbi
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new PostgresPlugin());
        if (config.isLoggingEnabled()) {
            jdbi.setSqlLogger(new DatabaseLogger());
        }

        // Run migrations
        if (config.isAutomatedMigrations()) {
            runMigrations(dataSource);
        }

        loadDAOs(jdbi, config.getDaoPackage());
    }

    public static <T> T dao(Class<T> c) {
        return map.getInstance(c);
    }

    private static DataSource lookupDataSource(DatabaseConfig.Connection connection) {
        try {
            Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
            return (DataSource) envCtx.lookup(connection.getDataSourceName());
        }
        catch (Exception ignored) {
            Log.info("Failed to get pooling DataSource from environment. Creating simple data source..");
        }
        try {
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(connection.getUrl());
            dataSource.setUser(connection.getUser());
            dataSource.setPassword(connection.getPassword());
            return dataSource;
        }
        catch (Exception e) {
            Log.error("Error creating simple data source", e);
            throw new RuntimeException("Could not initialize data source");
        }
    }

    private static void runMigrations(DataSource dataSource) {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }

    private static void loadDAOs(Jdbi jdbi, String daoPackage) {
        String prefix = daoPackage != null ? daoPackage : "";
        Set<Class<?>> daoClasses = new Reflections(prefix).getTypesAnnotatedWith(Dao.class);
        for (Class<?> c : daoClasses) {
            map.put(c, jdbi.onDemand(c));
        }
    }
}
