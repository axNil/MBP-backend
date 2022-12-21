package org.example;

import org.example.beans.UnicornInfo;

import java.util.Locale;

public class Utils {

    public static String createNameQuery(double lon, double lat) {
        return String.format(Locale.US,"""
                {
                "model": "text-davinci-003",
                "prompt": "Generate a name for a specific type of unicorn. Base the name on the surrounding area of longitude %.6f and latitude %.6f, don't include any numbers in the name.",
                "max_tokens": 100,
                "temperature": 1
                }
                """, lon, lat);
    }

    public static String createDescriptionQuery(UnicornInfo unicornInfo) {

        return "";
    }
}
