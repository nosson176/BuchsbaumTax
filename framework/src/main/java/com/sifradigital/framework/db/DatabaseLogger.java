package com.sifradigital.framework.db;

import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

public class DatabaseLogger implements SqlLogger {

    private static final Logger Log = LoggerFactory.getLogger(DatabaseLogger.class);

    @Override
    public void logAfterExecution(StatementContext context) {
        Log.debug("{}; took {} ms", context.getRenderedSql(), context.getElapsedTime(ChronoUnit.MILLIS));
    }
}
