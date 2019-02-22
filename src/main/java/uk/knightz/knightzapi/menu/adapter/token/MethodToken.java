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
import org.apache.commons.lang.StringUtils;
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.reflect.Reflection;

import java.lang.reflect.Method;

/**
 * Token that stores attributes about a Method, and a value of that Method
 *
 * @param <V> The type of Value (ie the type of the Method)
 */
@Data
public class MethodToken<V> implements DataToken<Method, V> {
    private final MethodAttributes<V> method;
    private V value;

    public MethodToken(MethodAttributes<V> method, V value) {
        this.method = method;
        this.value = value;
    }

    public MethodToken(Method method) {
        this(method, null);
    }

    public MethodToken(Method method, V v) {
        this.method = MethodAttributes.ofMethod(method);
        this.value = v;
    }


    @Override
    public String getFriendlyDataName() {
        return StringUtils.capitalize(getDataName()
                .substring(3)); //remove get
    }

    @Override
    public String getDataName() {
        return method.getName();
    }

    @Override
    public Class<V> getType() {
        if (value == null) {
            return null;
        }
        return (Class<V>) value.getClass();
    }

    @Override
    public boolean hasSettingFunctionality() {
        return method.getSetter() != null;
    }

    @Override
    public AbstractSetter getSetter() {
        return o -> Reflection.callMethod(method.getSetter(), value, o);
    }
}
