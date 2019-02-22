package uk.knightz.knightzapi.menu.adapter.options;


/**
 * Bitmask of boolean settings for the Tokenizer
 */
public class Settings {

    /**
     * If fields should be included
     * Disabled by default as fields are usually encapsulated
     * <p>
     * If the options byte contains {@link Settings#UNFRIENDLY}, that overrides this option
     */
    public static final int INCLUDE_FIELDS = 1;
    /**
     * If unfriendly data should be included
     * Unfriendly data includes all methods and all fields, regardless if they are private or not
     */
    public static final int UNFRIENDLY = 2;
    /**
     * If static methods (and fields if {@link Settings#INCLUDE_FIELDS} is set)
     * should be included.
     */
    public static final int INCLUDE_STATIC = 4;

    /**
     * Beta functionality. When enabled, allow the user to set the values of any data they receive
     */
    public static final int ALLOW_SETTING_VALUES = 8;

}