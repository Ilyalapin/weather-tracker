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


    private static String capitalize(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }
}
