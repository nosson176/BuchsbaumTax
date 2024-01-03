package com.sifradigital.framework.mail;

import com.sifradigital.framework.mail.model.ResolvedSendInfo;
import com.sifradigital.framework.mail.model.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Mail {

    private final static Logger Log = LoggerFactory.getLogger(Mail.class);

    private static MailService service;
    private static MailConfig config;

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void init(MailConfig config) {
        service = config.getService();
        Mail.config = config;
    }

    public static boolean send(Send send) {
        ensureInit();
        try {
            ResolvedSendInfo info = send.resolve(config);
            if (config.getFilter() != null && !config.getFilter().shouldSend(info.getToAddress())) {
                return false;
            }
            return service.send(info);
        }
        catch (Exception e) {
            Log.error("Error sending email", e);
            return false;
        }
    }

    public static void sendLater(Send send) {
        ensureInit();
        executor.execute(() -> send(send));
    }

    private static void ensureInit() {
        if (service == null || config == null) {
            throw new IllegalStateException("Mail not initialized!");
        }
    }
}
