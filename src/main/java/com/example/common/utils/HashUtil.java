package com.example.common.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }

    public static boolean verify(String plain, String hash) {
        return BCrypt.checkpw(plain, hash);
    }
}