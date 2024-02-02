package com.sifradigital.framework.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateToken {

    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        return new BigInteger(130, random).toString(32);
    }
}
