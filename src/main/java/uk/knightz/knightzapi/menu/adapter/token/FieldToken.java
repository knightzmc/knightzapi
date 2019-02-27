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
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.menu.adapter.token.types.AbstractSetter;
import uk.knightz.knightzapi.reflect.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Token that stores attributes about a Field, and a value of that field
 *
 * @param <V> The type of Value (ie the type of the Field)
 */
@Data
public class FieldToken<V> implements DataToken<Field, V> {

    private final FieldAttributes<V> field;
    private V value;

    public FieldToken(FieldAttributes<V> field, V value) {
        this.field = field;
        this.value = value;
    }

    public FieldToken(Field field, V value) {
        this.field = FieldAttributes.ofField(field);
        this.value = value;
    }

    public FieldToken(Field field) {
        this(field, null);
    }

    @Override
    public String getFriendlyDataName() {
        return getDataName();
    }

    @Override
    public String getDataName() {
        return field.getName();
    }

    @Override
    public Class<V> getType() {
        return (Class<V>) value.getClass();
    }

    @Override
    public boolean hasSettingFunctionality() {
        return !Modifier.isFinal(field.getModifiers());
    }

    @Override
    public AbstractSetter<V, Object> getSetter() {
        return (type, value) -> Reflection.setFieldValue(field.getField(), type, value);
    }
}
