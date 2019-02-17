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

package uk.knightz.knightzapi.menu.adapter.token.factory;

import uk.knightz.knightzapi.menu.adapter.ClassSearcher;
import uk.knightz.knightzapi.menu.adapter.Options;
import uk.knightz.knightzapi.menu.adapter.token.FieldToken;
import uk.knightz.knightzapi.menu.adapter.token.MethodToken;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.Tokenizer;
import uk.knightz.knightzapi.utils.Struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class TokenFactory<T> {

    public ObjectToken<T> generate(T t, Options options) {
        return generate(t, (Class<T>) t.getClass(), options);
    }

    private ObjectToken<T> generate(T t, Class<T> tClass, Options options) {
        ClassSearcher searcher = new ClassSearcher();
        searcher.setFriendly(options.isFriendly());
        Struct<List<Method>, List<Field>> data = searcher.dataOfClass(tClass, options);

        Struct<List<MethodToken>, List<FieldToken>> tokenStruct = Tokenizer.convert(data, t);

        return new ObjectToken<>(tokenStruct.getB(), tokenStruct.getA());
    }
}
