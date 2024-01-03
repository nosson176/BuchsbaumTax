package com.sifradigital.framework;

import com.sifradigital.framework.jobs.JobsConfig;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.knowm.sundial.SundialJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SifraApplication extends ResourceConfig {

    private static final Logger Log = LoggerFactory.getLogger(SifraApplication.class);

    public SifraApplication() {
        Log.info("Starting initialization of Sifra application");
        printBanner();
        // First add all framework classes for auto-discovery
        packages(true, "com.sifradigital.framework");
    }

    protected void scheduleJobs(JobsConfig config) {
        if (config.getJobsPackage() == null || config.getJobsPackage().isEmpty())
            return;

        int threadSize = config.getThreads() > 0 ? config.getThreads() : 10;
        SundialJobScheduler.createScheduler(threadSize, config.getJobsPackage());
        try {
            SundialJobScheduler.getScheduler().start();
        }
        catch (Exception e) {
            Log.error("Sundial Scheduler failed to initialize:", e);
        }
    }

    private void printBanner() {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource("banner.txt");
        if (resource != null) {
            try {
                String file = resource.getFile();
                String banner = FileUtils.readFileToString(new File(file), StandardCharsets.UTF_8);
                Log.info(banner);
            } catch (Exception ignored) {
            }
        }
    }
}
