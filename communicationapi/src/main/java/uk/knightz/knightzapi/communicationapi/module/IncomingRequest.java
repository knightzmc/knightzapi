package uk.knightz.knightzapi.communicationapi.module;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public final class IncomingRequest {
    public String getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    private final String data;
    private final String id;

    public IncomingRequest(String data, String id) {
        this.data = data;
        this.id = id;
    }
}
