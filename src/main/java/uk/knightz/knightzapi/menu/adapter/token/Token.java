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

/**
 * Any Token
 *
 * @param <O> The type of Token eg Object, Method, Field
 */
public interface Token<O> {


    /**
     * An extension of Token for {@link MethodToken} and {@link FieldToken}
     *
     * @param <O> The type of Token (Method or Field)
     * @param <V> The type of Object it stores
     */
    interface DataToken<O, V> extends Token {


        /**
         * @return A friendly name of the data. A method will have its "get" removed
         */
        String getFriendlyDataName();

        /**
         * @return A full data name of the data
         */
        String getDataName();

        /**
         * @return The type of Object this Token holds
         */
        Class<V> getType();

        /**
         * @return The actual value of the Token's data
         */
        V getValue();
    }

}
