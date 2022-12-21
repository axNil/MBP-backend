package org.example;

import org.example.beans.Location;
import org.example.beans.Unicorn;
import org.example.beans.UnicornInfo;

import java.util.Locale;

public class Utils {

    public static String createNameQuery(Location loc) {
        return String.format(Locale.US,"""
                {
                "model": "text-davinci-003",
                "prompt": "Generate a name for a specific type of unicorn. Base the name on the surrounding area of longitude %.6f and latitude %.6f, don't include any numbers in the name.",
                "max_tokens": 100,
                "temperature": 1
                }
                """, loc.lon, loc.lat);
    }

    public static String createDescriptionQuery(UnicornInfo unicornInfo, String name) {
        String str = String.format(Locale.US,"Generera en beskrivning från en encyklopedi av en sorts enhörning som heter %s." +
                        " Enhörningen har följande attribut: Färg: %s, hornet är %s." +
                        " Enhörningens beteende är beskrivet som: %s." +
                        " Inkludera information om hur enhörningen har anpassat sig till sin miljö kring %s"
                , name, unicornInfo.color, unicornInfo.horn, unicornInfo.behaviour, unicornInfo.spottedWhere.name);

        return String.format("""
                {
                "model": "text-davinci-003",
                "prompt": "%s",
                "max_tokens": 1500,
                "temperature": 1
                }
                """, str);

    }

    public static String createImageQuery(UnicornInfo info, String name) {
        String str = String.format("A photograph in the style of fantasy photography of a specific type of unicorn called %s " +
                "with %s colored mane and a horn that is %s. The background is the landscape of %s"
                , name, info.color, info.horn, info.spottedWhere.name);
        return String.format("""
                {
                   "prompt": "%s",
                   "n": 1,
                   "size": "512x512"
                 }
                """, str);
    }

    public static String createMultiImageQuery(Unicorn unicorn) {
        String str = String.format("A photograph in the style of fantasy photography of a specific type of unicorn called %s " +
                        "with the description: %s. The background is the landscape of %s"
                , unicorn.name, unicorn.description, unicorn.spottedWhere.name);
        return String.format("""
                {
                   "prompt": "%s",
                   "n": 2,
                   "size": "512x512"
                 }
                """, str);
    }


}
