package com.oasis.cac.vas.utils;

import java.io.InputStream;
import java.net.URL;

public class ResourceUtil {

    public static InputStream getResourceAsStream(String fileName) {
       System.out.println(fileName);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        return classloader.getResourceAsStream(fileName);
    }

    public static URL getResourceAsUrl(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResource(fileName);
    }
}
