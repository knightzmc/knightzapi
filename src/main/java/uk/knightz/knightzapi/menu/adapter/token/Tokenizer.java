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

package uk.knightz.knightzapi.menu.adapter.token;

import uk.knightz.knightzapi.reflect.Reflection;
import uk.knightz.knightzapi.utils.Struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts Methods and Fields into Tokens
 */
public class Tokenizer {

    /**
     * Convert 2 lists of Methods and Fields into 2 lists of MethodTokens and FieldTokens
     *
     * @param data               The Methods and Fields to convert
     * @param retrieveValuesFrom An Object that these methods and fields will be called on to retrieve their values
     * @return A Struct containing 2 converted lists
     */
    public static Struct<List<MethodToken>, List<FieldToken>> convert(Struct<List<Method>, List<Field>> data, Object retrieveValuesFrom) {
        List<MethodToken> mts = convertMethods(data.getA(), retrieveValuesFrom);
        List<FieldToken> fts = convertFields(data.getB(), retrieveValuesFrom);
        return new Struct<>(mts, fts);
    }

    private static List<FieldToken> convertFields(List<Field> b, Object retrieveValuesFrom) {
        if (b == null) return new ArrayList<>();
        List<FieldToken> fieldTokens = new ArrayList<>();
        for (Field field : b) {
            field.setAccessible(true);
            FieldToken e = new FieldToken(field);
            e.setValue(Reflection.valueOfField(field, retrieveValuesFrom));
            if (e.getValue() != null)
                fieldTokens.add(e);
        }
        return fieldTokens;
    }

    private static List<MethodToken> convertMethods(List<Method> a, Object retrieveValuesFrom) {
        List<MethodToken> methodTokens = new ArrayList<>();
        for (Method method : a) {
            method.setAccessible(true);
            MethodToken e = new MethodToken(method);
            e.setValue(Reflection.callMethod(method, retrieveValuesFrom));
            if (e.getValue() != null)
                methodTokens.add(e);
        }
        return methodTokens;
    }
}
