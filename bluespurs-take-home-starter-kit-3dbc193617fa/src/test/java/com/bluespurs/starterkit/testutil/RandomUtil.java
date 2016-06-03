package com.bluespurs.starterkit.testutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    public static final String SAMPLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "abcdefghijklmnopqrstuvwxyz" +
        "0123456789";

    private static final Random random = new Random();
    private static final List<String> usedEmails = new ArrayList<>();

    public static String getRandomString(int length) {
        char[] text = new char[length];

        for(int i = 0; i < length; i++) {
            text[i] = SAMPLE_CHARACTERS.charAt(random.nextInt(SAMPLE_CHARACTERS.length()));
        }

        return String.valueOf(text);
    }

    public static String getRandomString(int minLength, int maxLength) {
        return getRandomString(getRandomInt(minLength, maxLength));
    }

    public static int getRandomInt() {
        return random.nextInt();
    }

    public static int getRandomInt(int min, int max) {
        // The range of numbers is inclusive to min and max.
        return random.nextInt((max - min) + 1) + min;
    }

    public static String getRandomEmail() {
        // Emails are unique.
        String email;

        do {
            email = String.format("%s@%s.com", getRandomString(1, 20), getRandomString(1, 20));
        }
        while(usedEmails.contains(email.toLowerCase()));

        // Normalize the emails to lowercase.
        usedEmails.add(email.toLowerCase());
        return email;
    }

    public static String getRandomPassword() {
        return getRandomString(8, 72);
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}
