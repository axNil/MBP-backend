package org.example;

import com.google.gson.JsonSyntaxException;
import org.example.beans.Location;
import org.example.beans.Unicorn;
import org.example.beans.UnicornInfo;
import org.example.beans.UnicornNoID;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                        " Inkludera information om hur enhörningen har anpassat sig till sin miljö kring %s."
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

    public static void unicornPostValidator(UnicornNoID unicorn) {
        StringBuilder sb = new StringBuilder();
        if (unicorn.name.isEmpty()) sb.append("Name is missing\n");
        if (unicorn.reportedBy.isEmpty()) sb.append("Reported by is missing\n");
        if (unicorn.description.isEmpty()) sb.append("Description is missing\n");
        if (unicorn.image.isEmpty()) sb.append("Image url is missing\n");
        if (unicorn.spottedWhere.name.isEmpty()) sb.append("Location name is missing\n");
        if (unicorn.spottedWhere.lon < -180.0 || unicorn.spottedWhere.lon > 180.0) sb.append("Longitude is invalid\n");
        if (unicorn.spottedWhere.lat < -90.0 || unicorn.spottedWhere.lat > 90.0) sb.append("Latitude is invalid\n");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            df.parse(unicorn.spottedWhen);
        } catch (DateTimeParseException e) {
            sb.append("Invalid or missing date");
        }
        if (!sb.isEmpty()) {
            throw new JsonSyntaxException(sb.toString());
        }
    }

    public static void unicornInfoValidator(UnicornInfo info) {
        StringBuilder sb = new StringBuilder();
        if (info.color.isEmpty()) sb.append("Color is missing\n");
        if (info.horn.isEmpty()) sb.append("Horn description is missing\n");
        if (info.reportedBy.isEmpty()) sb.append("Reported by is missing\n");
        if (info.behaviour.isEmpty()) sb.append("Behaviour description is missing\n");
        if (info.spottedWhere.name.isEmpty()) sb.append("Location name is missing\n");
        if (info.spottedWhere.lon < -180.0 || info.spottedWhere.lon > 180.0) sb.append("Longitude is invalid\n");
        if (info.spottedWhere.lat < -90.0 || info.spottedWhere.lat > 90.0) sb.append("Latitude is invalid\n");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            df.parse(info.spottedWhen);
        } catch (DateTimeParseException e) {
            sb.append("Invalid or missing date");
        }
        if (!sb.isEmpty()) {
            throw new JsonSyntaxException(sb.toString());
        }
    }
}
