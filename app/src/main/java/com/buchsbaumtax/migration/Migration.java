package com.buchsbaumtax.migration;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.sql.Connection;
import java.sql.DriverManager;

public class Migration {
    protected Handle handle;

    protected Migration() {
        handle = getHandle();
    }

    private Handle getHandle() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/buchsbaum", "postgres", "sifra123");
            Jdbi jdbi = Jdbi.create(connection);
            jdbi.installPlugin(new SqlObjectPlugin());
            return jdbi.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean castToBoolean(String str) {
        return str.equals("1") || str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes");
    }

    protected int castToInt(String str) {
        try {
            return Integer.parseInt(str);
        }
        catch (Exception e) {
            return -1;
        }
    }
}
