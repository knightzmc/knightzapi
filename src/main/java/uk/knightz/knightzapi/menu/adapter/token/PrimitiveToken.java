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

import uk.knightz.knightzapi.menu.adapter.token.types.AbstractGetter;
import uk.knightz.knightzapi.menu.adapter.token.types.AbstractSetter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A thread-safe subclass of ObjectToken to represent a primitive type.
 * These don't have any methods or fields so have to be handled a bit differently.
 * We create a fake "getValue()" method which gives us the primitiveValue of the primitive
 *
 * @param <T> The Object wrapper of primitive, eg {@link Integer}
 */
public class PrimitiveToken<T> extends ObjectToken<T> {

    private AtomicReference<T> primitiveValue;

    public PrimitiveToken(T value) {
        super(new ArrayList<>(), new ArrayList<>());
        this.primitiveValue = new AtomicReference<>(value);

        AbstractGetter getter = clazz -> primitiveValue.get();
        AbstractSetter<T, Object> setter = (type, trueValue) -> {
            primitiveValue.set(type);
        };
        GetterAttributes<T> getterAttributes = new GetterAttributes<>(getter, setter, "getValue", 1);
        this.methodTokens.add(new MethodToken<>(getterAttributes, value));
    }

}
