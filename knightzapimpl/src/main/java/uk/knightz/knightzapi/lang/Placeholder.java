package uk.knightz.knightzapi.lang;

/**
 * This class was created by AlexL (Knightz) on 05/03/2018 at 19:24.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public abstract class Placeholder {
    private final String placeholder;

    public Placeholder(String placeholder) {
        if (placeholder == null) {
            throw new IllegalArgumentException("Placeholder cannot be null!");
        }
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public abstract String getReplacement();


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
        return
                toReplace
                        .replace
                                (getPlaceholder(), getReplacement());
    }
}
