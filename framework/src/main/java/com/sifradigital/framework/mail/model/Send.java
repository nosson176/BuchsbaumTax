package com.sifradigital.framework.mail.model;

import com.sifradigital.framework.mail.MailConfig;

public interface Send {

    ResolvedSendInfo resolve(MailConfig config);
}
