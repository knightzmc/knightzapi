package uk.knightz.knightzapi.lang;

/**
 * This class was created by AlexL (Knightz) on 05/03/2018 at 19:24.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Placeholder {
    private final String placeholder;

    private Replacement replacement;

    public Placeholder(String placeholder, Replacement replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public String getPlaceholder() {
        return placeholder;
    }


    public Replacement replacement() {
        return replacement;
    }


    /**
     * Apply the placeholders to the given String
     *
     * @param toReplace
     * @return
     */
    public String replace(String toReplace) {
        if (toReplace == null) {
            return null;
        }
        //noinspection deprecation
        return
                toReplace
                        .replace
                                (getPlaceholder(), replacement.getReplacement());
    }

    public interface Replacement {
        String getReplacement();
    }
}
