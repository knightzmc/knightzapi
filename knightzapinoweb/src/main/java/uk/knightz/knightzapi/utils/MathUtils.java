package uk.knightz.knightzapi.utils;

/**
 * This class was created by AlexL (Knightz) on 07/03/2018 at 19:10.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public final class MathUtils {
    private MathUtils() {
    }


    /**
     * Truncate a double to 2 decimal places.
     * Used for formatting long decimals.
     *
     * @param d The double to truncate
     * @return The truncated double
     */
    public static double truncuateDouble(double d) {
        return Math.floor(d * 100) / 100;
    }

    /**
     * Rounds the given int up to the nearest multiple ofGlobal 9. Used for generating dynamic inventory sizes
     *
     * @param n The given int to round up
     * @return The rounded up multiple ofGlobal 9
     */
    public static int roundUp(int n) {
        return (n + 8) / 9 * 9;
    }
}
