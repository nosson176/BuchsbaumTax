package com.sifradigital.framework.mail.filter;

import java.util.HashSet;
import java.util.Set;

public class BlacklistSendFilter implements SendFilter {

    private final Set<String> blacklist = new HashSet<>();

    public void add(String address) {
        blacklist.add(address);
    }

    @Override
    public boolean shouldSend(String toAddress) {
        return !blacklist.contains(toAddress);
    }
}
