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

package uk.knightz.knightzapi.lang.fancy.fancyutils;

import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class BasecomponentReflection {
    private static final Field BASECOMPONENT_CURRENT, BASECOMPONENT_PARTS;

    static {
        Field BASECOMPONENT_PARTS1;
        Field BASECOMPONENT_CURRENT1;
        try {
            BASECOMPONENT_CURRENT1 = ComponentBuilder.class.getDeclaredField("current");
            BASECOMPONENT_PARTS1 = ComponentBuilder.class.getDeclaredField("parts");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            BASECOMPONENT_CURRENT1 = null;
            BASECOMPONENT_PARTS1 = null;
        }
        BASECOMPONENT_PARTS = BASECOMPONENT_PARTS1;
        BASECOMPONENT_CURRENT = BASECOMPONENT_CURRENT1;

        Objects.requireNonNull(BASECOMPONENT_PARTS).setAccessible(true);
        BASECOMPONENT_CURRENT.setAccessible(true);
    }

    @SneakyThrows
    public static BaseComponent currentOf(ComponentBuilder builder) {
        return (BaseComponent) BASECOMPONENT_CURRENT.get(builder);
    }

    @SneakyThrows
    public static List<BaseComponent> partsOf(ComponentBuilder builder) {
        return (List<BaseComponent>) BASECOMPONENT_PARTS.get(builder);
    }

    @SneakyThrows
    public static ComponentBuilder setCurrent(ComponentBuilder builder, BaseComponent current) {
        BASECOMPONENT_CURRENT.set(builder, current);
        return builder;
    }

    @SneakyThrows
    public static ComponentBuilder setParts(ComponentBuilder builder, List<BaseComponent> parts) {
        BASECOMPONENT_PARTS.set(builder, parts);
        return builder;
    }
}
