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

package uk.knightz.knightzapi.menu.adapter.token;

import lombok.Data;

import java.util.List;

/**
 * Data class to represent the field structure and values of an object in a serializing friendly way
 * An example data class:
 * {
 * name: "Steve",
 * age: 27
 * }
 * would be converted to an ObjectToken looking like this:
 * {@code
 * {
 * tokens:
 * [
 * FieldToken<String>
 * {
 * name.field,"Steve"
 * },
 * FieldToken<Integer>
 * {
 * age.field, 27
 * }
 * ]
 * }
 * }
 */
@Data
public class ObjectToken<T> implements Token<Object> {

    protected final List<FieldToken> fieldTokens;

    protected final List<MethodToken> methodTokens;

    public boolean hasNoValues() {
        return fieldTokens.isEmpty() && methodTokens.isEmpty();
    }
}
