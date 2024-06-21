package com.stellatech.elopezo.kms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String maskCard(String text) {
        String regexAmex = "(\\b(\\d{6})\\d+(\\d{5}))(.*)";
        String maskAmex = "$2xxxx$3";
        String regex = "(\\b(\\d{6})\\d+(\\d{4}))(.*)";
        String mask = "$2xxxxxx$3";
        text = (text.length() > 15 ? text.replaceAll(regex, mask) : text.replaceAll(regexAmex, maskAmex)).replaceAll("F", "").replaceAll("f", "");
        return text;
    }

    public static String serializerCard(String cardNumber, Date date, String cvv) {
        return String.format("%s=%s%s", cardNumber, formatDate(date), (cvv != null) ? cvv : "");
    }

    public static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
            return formatter.format(date);
        } else {
            return "";
        }
    }
}
