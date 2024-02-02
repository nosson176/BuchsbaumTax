package com.sifradigital.framework.mail;

import com.sifradigital.framework.mail.model.ResolvedSendInfo;

public interface MailService {

    boolean send(ResolvedSendInfo send);
}
