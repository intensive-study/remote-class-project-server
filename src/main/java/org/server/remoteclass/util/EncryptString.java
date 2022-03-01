package org.server.remoteclass.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptString {
    public static final String DELIMITER = "-";
    public static final int NEEDED = 1;

    public static String encryptThisString(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));

            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }

            return hashText.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // order: Lecture, Member
    public static String convertedToEncryption(Long arg1, Long arg2) {
        String s = changeLongToString(arg1, arg2);
        return encryptThisString(s);
    }

    public static Long decrypt(String target) {
        String[] splited = target.split(DELIMITER);
        if(splited.length != 2) return null;
        return Long.parseLong(splited[NEEDED]);
    }

    public static String changeLongToString(Long arg1, Long arg2) {
        return arg1 + DELIMITER + arg2;
    }

    public static String changeLongToString(Long arg) {
        return Long.toString(arg);
    }

}
