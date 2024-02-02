package com.sifradigital.framework.db;

public class DatabaseConfig {

    private final Connection connection;
    private final String daoPackage;
    private final boolean automatedMigrations;
    private final boolean loggingEnabled;

    public DatabaseConfig(Connection connection, String daoPackage, boolean automatedMigrations, boolean loggingEnabled) {
        this.connection = connection;
        this.daoPackage = daoPackage;
        this.automatedMigrations = automatedMigrations;
        this.loggingEnabled = loggingEnabled;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public boolean isAutomatedMigrations() {
        return automatedMigrations;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public static class Connection {

        private final String dataSourceName;
        private final String url;
        private final String user;
        private final String password;

        public Connection(String dataSourceName, String url, String user, String password) {
            this.dataSourceName = dataSourceName;
            this.url = url;
            this.user = user;
            this.password = password;
        }

        public Connection(String dataSourceName, String localDbName) {
            this.dataSourceName = dataSourceName;
            this.url = "jdbc:postgresql://buchsbaumdb.c1w2s6uqe11n.il-central-1.rds.amazonaws.com:5432/" + localDbName;
            // this.url = "jdbc:postgresql://localhost:5432/" + localDbName;
            this.user = "postgres";
            this.password = "sifra123";
        }

        public Connection(String dataSourceName) {
            this.dataSourceName = dataSourceName;
            this.url = null;
            this.user = null;
            this.password = null;
        }

        public String getDataSourceName() {
            return dataSourceName;
        }

        public String getUrl() {
            return url;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }
    }
}
