package com.sifradigital.framework.mail;

import com.sifradigital.framework.mail.model.ResolvedSendInfo;
import com.sifradigital.framework.mail.model.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SMS {

    private final static Logger Log = LoggerFactory.getLogger(Mail.class);

    private static MailService service;
    private static MailConfig config;

    private static final Executor executor = Executors.newFixedThreadPool(4);

    public static void init(MailConfig config) {
        service = config.getService();
        SMS.config = config;
    }

    public static boolean send(Send send) {
        ensureInit();
        try {
            ResolvedSendInfo info = send.resolve(config);
            if (config.getFilter() != null && !config.getFilter().shouldSend(info.getToAddress())) {
                Log.warn("SMS to {} filtered out by configuration", info.getToAddress());
                return false;
            }
            return service.send(info);
        } catch (Exception e) {
            Log.error("Error sending SMS to {}: {}", e.getMessage(), e);
            return false;
        }
    }

    public static void sendLater(Send send) {
        ensureInit();
        executor.execute(() -> send(send));
    }

    private static void ensureInit() {
        if (service == null || config == null) {
            throw new IllegalStateException("SMS not initialized!");
        }
    }
}
