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

package uk.knightz.knightzapi.utils;

import com.google.common.primitives.Chars;
import lombok.val;
import org.apache.commons.lang.CharUtils;

import java.util.Arrays;

public class NumberBuilder {
    private Number[] numbers;


    public NumberBuilder() {
        numbers = new Number[]{};
    }

    public static NumberBuilder ofString(String s) {
        val builder = new NumberBuilder();
        for (Character next : Chars.asList(s.toCharArray())) {
            if (CharUtils.isAsciiNumeric(next)) {
                builder.append(CharUtils.toIntValue(next));
            } else {
                break;
            }
        }
        return builder;
    }

    /**
     * Append a Number to the Builder
     * Only positive numbers are allowed, and so the absolute value of the given number is used
     *
     * @param n the Number to append
     * @return this Builder
     */
    public NumberBuilder append(Number n) {
        if (n == null) return this;
        n = Math.abs(n.longValue());
        Number[] newNumbers = Arrays.copyOf(numbers, numbers.length + 1);
        newNumbers[this.numbers.length] = n;
        this.numbers = newNumbers;
        return this;
    }

    public NumberBuilder backspace() {
        if (isEmpty()) return this;
        this.numbers = Arrays.copyOf(numbers, numbers.length - 1);
        return this;
    }

    public long toLong() {
        return Long.valueOf(toString());
    }

    public int toInt() {
        return Integer.valueOf(toString());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Number number : numbers) {
            builder.append(number.longValue());
        }
        return builder.toString();
    }

    public int length() {
        return numbers.length;
    }


    /**
     * Check if this builder is empty
     * Normally an array of objects would loop and check if they are all null,
     * but we don't allow any null entries. The only way to edit values changes the array length.
     * However, the builder could be all zeros (000000)
     *
     * @return if this builder is empty
     */
    public boolean isEmpty() {
        return length() == 0;
    }
}
