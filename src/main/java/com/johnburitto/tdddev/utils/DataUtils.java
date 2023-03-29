package com.johnburitto.tdddev.utils;

public class DataUtils {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        phoneNumber = clearPhoneNumber(phoneNumber);

        return lengthValidation(phoneNumber) && contentValidation(phoneNumber);
    }

    private static String clearPhoneNumber(String phoneNumber) {
        return phoneNumber
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(" ", "")
                .replace("+", "");
    }

    private static boolean lengthValidation(String phoneNumber) {
        return phoneNumber.length() > 2 && phoneNumber.length() < 13;
    }

    private static boolean contentValidation(String phoneNumber) {
        String numbers = "0123456789";

        for (int i = 0; i < phoneNumber.length(); i++) {
            if (numbers.indexOf(phoneNumber.charAt(i)) == -1) {
                return false;
            }
        }

        return true;
    }
}
