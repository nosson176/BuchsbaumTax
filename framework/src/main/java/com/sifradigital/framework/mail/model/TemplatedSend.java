package com.sifradigital.framework.mail.model;

import com.sifradigital.framework.mail.MailConfig;
import com.sifradigital.framework.util.ResourceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplatedSend extends AbstractSend {

    private final static Map<String, String> templateCache = new HashMap<>();

    private static String masterTemplate = "%content%";

    private final String replyToAddress;
    private final String templateName;
    private final Map<String, String> substitutions;
    private final List<Attachment> attachments;

    private Map<String, String> mergeTags;

    public static void setMasterTemplate(String masterTemplate) {
        TemplatedSend.masterTemplate = masterTemplate;
    }

    public TemplatedSend(String toAddress, String subject, String templateName) {
        this(null, null, toAddress, subject, templateName, new HashMap<>());
    }

    public TemplatedSend(String toAddress, String subject, String templateName, Map<String, String> substitutions) {
        this(null, null, toAddress, subject, templateName, substitutions);
    }

    public TemplatedSend(String toAddress, String subject, String templateName, Map<String, String> substitutions, List<Attachment> attachments) {
        this(null, null, null, toAddress, subject, templateName, substitutions, attachments);
    }

    public TemplatedSend(String fromAddress, String fromName, String toAddress, String subject, String templateName) {
        this(fromAddress, fromName, toAddress, subject, templateName, new HashMap<>());
    }

    public TemplatedSend(String fromAddress, String fromName, String toAddress, String subject, String templateName, Map<String, String> substitutions) {
        this(fromAddress, fromName, null, toAddress, subject, templateName, substitutions, new ArrayList<>());
    }

    public TemplatedSend(String fromAddress, String fromName, String replyToAddress, String toAddress, String subject, String templateName, Map<String, String> substitutions) {
        this(fromAddress, fromName, replyToAddress, toAddress, subject, templateName, substitutions, new ArrayList<>());
    }

    public void setMergeTags(Map<String, String> mergeTags) {
        this.mergeTags = mergeTags;
    }

    public TemplatedSend(String fromAddress, String fromName, String replyToAddress, String toAddress, String subject, String templateName, Map<String, String> substitutions, List<Attachment> attachments) {
        super(fromAddress, fromName, toAddress, subject);
        this.replyToAddress = replyToAddress;
        this.templateName = templateName;
        this.substitutions = substitutions;
        this.attachments = attachments;
    }

    @Override
    public ResolvedSendInfo resolve(MailConfig config) {
        String body = renderTemplate(templateName, substitutions, config);
        String fromAddress = getFromAddress() != null ? getFromAddress() : config.getDefaultFromAddress();
        String fromName = getFromName() != null ? getFromName() : config.getDefaultFromName();
        return new ResolvedSendInfo(fromAddress, fromName, replyToAddress, getToAddress(), getSubject(), body, "text/html", attachments, mergeTags);
    }

    private String renderTemplate(String templateName, Map<String, String> substitutions, MailConfig config) {
        String template = templateCache.computeIfAbsent(templateName, n -> getTemplate(n, config));
        if (template == null) {
            return null;
        }
        template = masterTemplate.replace("%content%", template);
        for (String key : substitutions.keySet()) {
            template = template.replace(key, substitutions.get(key));
        }
        return template;
    }

    private String getTemplate(String templateName, MailConfig config) {
        String path = config.getTemplatePath() != null ? config.getTemplatePath() + "/" + templateName : templateName;
        return ResourceUtils.resourceAsString(path);
    }
}
