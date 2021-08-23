package com.buchsbaumtax.app.config;

import com.sifradigital.framework.CORSFilter;
import com.sifradigital.framework.SifraApplication;
import com.sifradigital.framework.db.Database;
import com.sifradigital.framework.db.DatabaseConfig;

public class BuchsbaumApplication extends SifraApplication {

   public BuchsbaumApplication() {
       packages(true, "com.buchsbaumtax.app.resource");

       register(new CORSFilter());

       DatabaseConfig.Connection connection = new DatabaseConfig.Connection("jdbc/BTDB", "jdbc:postgresql://localhost:5432/buchsbaum", "postgres", "sifra123");
       DatabaseConfig databaseConfig = new DatabaseConfig(connection, "com.buchsbaumtax.core.dao", false, false);
       Database.init(databaseConfig);
   }
}
