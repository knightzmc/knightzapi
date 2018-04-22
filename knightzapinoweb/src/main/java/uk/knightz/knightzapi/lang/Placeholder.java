package uk.knightz.knightzapi.lang;

/**
 * This class was created by AlexL (Knightz) on 05/03/2018 at 19:24.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * A placeholder is an object that can easily replace Strings with dynamic content.
 **/
public class Placeholder {
    /**
     * The placeholder string that will be replaced, eg "%username%"
     */
    private final String placeholder;

    /**
     * The method(s) that will be called when replacing the placeholder, eg "User::getName"
     */
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
     * @param toReplace The string to replace any instances of Placeholder#getPlaceholder with Placeholder#getReplacement
     * @return The formatted String
     */
    public String replace(String toReplace) {
        if (toReplace == null) {
            return null;
        }
        return toReplace.replace(getPlaceholder(), replacement.getReplacement());
    }

    public interface Replacement {
        String getReplacement();
    }
}
