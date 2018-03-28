package uk.knightz.knightzapi.communication.authorisation;

/**
 * This class was created by AlexL (Knightz) on 17/02/2018 at 15:11.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class NotAuthorisedException extends Exception {
    public NotAuthorisedException() {
    }

    public NotAuthorisedException(String message) {
        super(message);
    }

    public NotAuthorisedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthorisedException(Throwable cause) {
        super(cause);
    }

    public NotAuthorisedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
