package com.sifradigital.framework.auth;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

public class CachingAuthenticator<C, P extends Principal> implements Authenticator<C, P> {

    private final LoadingCache<C, P> cache;

    public CachingAuthenticator(Authenticator<C, P> authenticator) {
        this(authenticator, Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000));
    }

    public CachingAuthenticator(Authenticator<C, P> authenticator, Caffeine<Object, Object> builder) {
        this.cache = builder.build(authenticator::authenticate);
    }

    @Override
    public P authenticate(C credentials) {
        return cache.get(credentials);
    }
}
