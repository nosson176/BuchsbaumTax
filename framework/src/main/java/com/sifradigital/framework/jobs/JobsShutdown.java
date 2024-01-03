package com.sifradigital.framework.jobs;

import org.knowm.sundial.SundialJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class JobsShutdown implements ServletContextListener {

    private static final Logger Log = LoggerFactory.getLogger(JobsShutdown.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            if (SundialJobScheduler.getScheduler() != null) {
                SundialJobScheduler.getScheduler().shutdown();
            }
        }
        catch (Exception e) {
            Log.error("Sundial Scheduler failed to shutdown cleanly: ", e);
        }
    }
}

