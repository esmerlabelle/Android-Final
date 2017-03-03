package com.example.elabelle.cp282final.helpers;

import java.util.regex.Pattern;

/**
 * Created by elabelle on 3/2/17.
 */

public class RegexPatternsConstants {

    // Pattern for gathering @usernames
    public static final Pattern SCREEN_NAME = Pattern.compile("(@[a-zA-Z0-9_]+)");

    // Pattern for gathering #hashtags
    public static final Pattern HASH_TAG = Pattern.compile("^#{1}[^\\p{Z}]+");

    // Pattern for gathering http:// links
    public static final Pattern HYPER_LINK = Pattern
            .compile("([Hh][tT][tT][pP][sS]?:\\/\\/)*([wW][wW][wW]\\.)*([^@\\s])+\\.[a-zA-Z]{2,6}(\\/(\\S)*)*");

    // Pattern for gathering e-mail
    public static final Pattern EMAIL = Pattern
            .compile("[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");

    // Pattern for gathering phone numbers
    public static final Pattern PHONE = Pattern.compile("([\\+|0{2}][\\d]{1,3})?[\\d]{6,11}|1?(?:[.\\s-]?[2-9]\\d{2}[" +
            ".\\s-]?|\\s?\\([2-9]\\d{2}\\)\\s?)(?:[1-9]\\d{2}[.\\s-]?\\d{4}\\s?(?:\\s?([xX]|[eE][xX]|[eE][xX]\\" +
            ".|[eE][xX][tT]|[eE][xX][tT]\\.)\\s?\\d{3,4})?|[a-zA-Z]{7})");

    // Pattern for gathering IP adresses
    public static final Pattern IP_ADDRESS = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Insert here the pattern you want to match in EditText
     */
    public static final Pattern[] patterns = {HYPER_LINK, EMAIL, PHONE, HASH_TAG};
}