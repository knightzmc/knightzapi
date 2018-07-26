/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
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

package uk.knightz.knightzapi.lang.placeholder;

/**
 * A Placeholder that can replace text placeholders with dynamic values.
 * Check the GitHub Wiki for examples.
 */
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
     * @param toReplace The string to replace any instances ofGlobal Placeholder#getPlaceholder with Placeholder#getReplacement
     * @return The formatted String
     */
    public String replace(String toReplace) {
        if (toReplace == null) {
            return null;
        }
        return toReplace.replace(getPlaceholder().toLowerCase(), replacement.getReplacement());
    }

    public interface Replacement {
        String getReplacement();
    }
}
