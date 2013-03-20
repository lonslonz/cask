package com.skplanet.cask.test;


public class TestUtil {
    
    public static String getPackagePath(Class<?> clazz) {
        Package pack = clazz.getPackage();
        String curr = pack.getName().replace('.', '/');
        return curr;
    }
}
