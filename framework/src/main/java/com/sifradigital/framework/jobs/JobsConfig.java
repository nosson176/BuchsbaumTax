package com.sifradigital.framework.jobs;

public class JobsConfig {

    private String jobsPackage;
    private int threads;

    public JobsConfig() {

    }

    public JobsConfig(String jobsPackage, int threads) {
        this.jobsPackage = jobsPackage;
        this.threads = threads;
    }

    public int getThreads() {
        return threads;
    }

    public String getJobsPackage() {
        return jobsPackage;
    }

    public void setJobsPackage(String jobsPackage) {
        this.jobsPackage = jobsPackage;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
