package com.bookserve.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {
    public static String md5Encrypt(String password) {
        return DigestUtils.md5Hex(password);
    }
}
