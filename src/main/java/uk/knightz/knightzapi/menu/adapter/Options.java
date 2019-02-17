/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.menu.adapter;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Options {

    public static final Options defaultOptions = new Options(
            Settings.create(Settings.READ_ONLY),
            Collections.emptyList(),
            Collections.emptyList(),
            0
    );
    private final int settings;
    private final List<String> methodBlacklist;
    private final List<String> fieldBlacklist;
    private final int modifierBlacklist;
    private boolean includeFields = false;
    private boolean friendly = true;

    public static class Settings {
        public static int READ_ONLY = 1;
//        public static int INCLUDE_CLASS_CLASS = 2;

        private Settings() {
        }

        public static boolean isReadOnly(int settings) {
            return (settings & READ_ONLY) != 0;
        }

//        public static boolean includesClassClass(int settings) {
//            return (settings & INCLUDE_CLASS_CLASS) != 0;
//        }

        public static int create(int... settings) {
            int x = 0;
            for (int o : settings) {
                x |= o;
            }
            return x;
        }
    }
}
