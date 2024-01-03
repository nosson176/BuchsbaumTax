package com.sifradigital.framework.mail.filter;

public interface SendFilter {

    boolean shouldSend(String toAddress);
}
