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
     *
     * @param n the Number to append
     * @return this Builder
     */
    public NumberBuilder append(Number n) {
        Number[] numbers = Arrays.copyOf(this.numbers, this.numbers.length + 1);
        numbers[this.numbers.length] = n;
        this.numbers = numbers;
        return this;
    }

    public long toLong() {
        StringBuilder buffer = new StringBuilder();
        for (Number number : numbers) {
            buffer.append(number.longValue());
        }
        return Long.valueOf(buffer.toString());
    }

    public int toInt() {
        StringBuilder buffer = new StringBuilder();
        for (Number number : numbers) {
            buffer.append(number.intValue());
        }
        return Integer.valueOf(buffer.toString());
    }
}
