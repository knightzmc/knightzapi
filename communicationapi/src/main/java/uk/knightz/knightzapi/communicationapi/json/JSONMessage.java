package uk.knightz.knightzapi.communicationapi.json;

import com.google.gson.Gson;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:48.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class JSONMessage {
    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public JSONMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
