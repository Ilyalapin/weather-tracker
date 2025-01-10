package com.weather_tracker.commons.util;

public class FormatingUserInputUtil {

    public static String format(String input) {
        if (ValidationUtil.isValid(input)) {
            String[] words = input.split(" ");

            for (int i = 0; i < words.length; i++) {
                words[i] = capitalize(words[i]);
            }
            return String.join("-", words);
        }
        return input;
    }


    private static String capitalize(String phrase) {
        if (phrase == null || phrase.isEmpty()) {
            return phrase;
        }
        String[] words = phrase.split("[- ]");
        StringBuilder formattedPhrase = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (words[i].isEmpty()) continue;

            formattedPhrase.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1).toLowerCase());

            if (i < words.length - 1) {
                if (phrase.charAt(phrase.indexOf(words[i]) + words[i].length()) == '-') {
                    formattedPhrase.append("-");
                } else {
                    formattedPhrase.append(" ");
                }
            }
        }
        return formattedPhrase.toString();
    }
}
