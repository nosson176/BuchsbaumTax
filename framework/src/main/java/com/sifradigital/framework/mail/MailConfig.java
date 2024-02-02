package com.sifradigital.framework.mail;

import com.sifradigital.framework.mail.filter.SendFilter;

public class MailConfig {

    private final MailService service;
    private final String defaultFromAddress;
    private final String defaultFromName;
    private final String templatePath;
    private SendFilter filter;

    public MailConfig(MailService service) {
        this(service, null, null, null);
    }

    public MailConfig(MailService service, String defaultFromAddress) {
        this(service, defaultFromAddress, null, null);
    }

    public MailConfig(MailService service, String defaultFromAddress, String defaultFromName, String templatePath) {
        this.service = service;
        this.defaultFromAddress = defaultFromAddress;
        this.defaultFromName = defaultFromName;
        this.templatePath = templatePath;
    }

    public MailService getService() {
        return service;
    }

    public String getDefaultFromAddress() {
        return defaultFromAddress;
    }

    public String getDefaultFromName() {
        return defaultFromName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public SendFilter getFilter() {
        return filter;
    }

    public void setFilter(SendFilter filter) {
        this.filter = filter;
    }
}
