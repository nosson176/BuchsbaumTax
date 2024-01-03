package com.sifradigital.framework.util;

import com.github.slugify.Slugify;

public class UrlUtils {

    private static final Slugify slugify = new Slugify();

    public static String slugify(String s) {
        return slugify.slugify(s);
    }
}
