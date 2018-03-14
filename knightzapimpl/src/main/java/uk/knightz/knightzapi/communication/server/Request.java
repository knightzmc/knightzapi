package uk.knightz.knightzapi.communication.server;

import java.util.Base64;

/**
 * This class was created by AlexL (Knightz) on 14/03/2018 at 21:50.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Custom implementation of <code>spark.Request</code> that automatically decrypts any recieved data.
 *
 * @see spark.Request
 */
public class Request extends spark.Request {

    @Override
    public String queryParams(String message) {
        return new String((Base64.getDecoder().decode(super.queryParams(message).getBytes())));
    }
}
