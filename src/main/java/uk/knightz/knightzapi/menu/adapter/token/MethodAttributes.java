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

import lombok.Data;
import uk.knightz.knightzapi.reflect.Reflection;

import java.lang.reflect.Method;

/**
 * A thread-safe store of some of the attributes of a certain Method object
 * It doesn't store the return type of the Method because when stored in a Method token,
 * the return type can be obtained with {@link MethodToken#getValue()} {@link Object#getClass()}
 */
@Data
public class MethodAttributes<T> {

    /**
     * Nullable
     */
    private transient final Method method;
    /**
     * Nullable
     */
    private transient final Method setter;
    private final String name;
    private final int modifiers;

    public static <V> MethodAttributes<V> ofMethod(Method method) {
        return new MethodAttributes<>(method, Reflection.getSetterOfGetter(method), method.getName(), method.getModifiers());
    }
}
