package com.sifradigital.framework.mail.filter;

import java.util.HashSet;
import java.util.Set;

public class WhitelistSendFilter implements SendFilter {

    private final Set<String> whitelist = new HashSet<>();

    public void add(String address) {
        whitelist.add(address);
    }

    @Override
    public boolean shouldSend(String toAddress) {
        return whitelist.contains(toAddress);
    }
}
