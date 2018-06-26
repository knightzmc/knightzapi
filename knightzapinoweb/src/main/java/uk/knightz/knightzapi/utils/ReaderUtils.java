package uk.knightz.knightzapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class ReaderUtils {

    private ReaderUtils() {}

    public static String readAllLines(Reader reader) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader b = new BufferedReader(reader)) {
            StringBuilder buffer = new StringBuilder();
            int r;
            while ((r = b.read()) != -1) {
                buffer.append((char) r);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}