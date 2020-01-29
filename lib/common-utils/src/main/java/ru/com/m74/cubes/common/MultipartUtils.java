package ru.com.m74.cubes.common;


import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

public class MultipartUtils {
    public static String decode(String str) {
        if (str == null) return null;

        try {
            str = new String(str.getBytes("iso8859-1"));
        } catch (UnsupportedEncodingException ignored) {
        }

        try {
            return MimeUtility.decodeText(str);
        } catch (UnsupportedEncodingException e) {
            return str;
        }

    }
}
