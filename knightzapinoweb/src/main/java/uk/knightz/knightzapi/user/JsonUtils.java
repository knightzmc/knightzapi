package uk.knightz.knightzapi.user;

import com.google.gson.JsonParser;
import org.apache.commons.lang.Validate;
import uk.knightz.knightzapi.KnightzAPI;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class JsonUtils {
    private static final JsonParser parser = new JsonParser();

    private JsonUtils() {
    }

    public static String prettifyJson(String json) {
        Validate.notNull(json, "String cannot be null");
        return KnightzAPI.gson.toJson(parser.parse(json));
    }
}
