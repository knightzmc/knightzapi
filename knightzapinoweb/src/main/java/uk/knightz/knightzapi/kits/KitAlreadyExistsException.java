package uk.knightz.knightzapi.kits;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:19.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class KitAlreadyExistsException extends RuntimeException {

    public KitAlreadyExistsException(String name) {
        super(name);
    }

    public KitAlreadyExistsException() {
        super();
    }
}