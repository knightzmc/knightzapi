/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reader related utility class
 */
public class ReaderUtils {

    private ReaderUtils() {}

    /**
     * Read all lines from a given reader and append it to a String
     * @param reader The Reader to read
     * @return All lines from the given reader as a StringC
     */
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