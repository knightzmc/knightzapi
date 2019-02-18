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

package uk.knightz.knightzapi.menu.adapter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import uk.knightz.knightzapi.menu.adapter.iface.UnfriendlyFilter;
import uk.knightz.knightzapi.menu.adapter.options.Options;
import uk.knightz.knightzapi.utils.Struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.google.common.cache.CacheBuilder.newBuilder;

/**
 * Class responsible for searching a given Class and constructing a List of Field and Method objects that it has
 */
public class ClassSearcher {

    /**
     * Cache of all the data of a Class
     * That is, all declared methods, and all declared fields
     */
    private static final LoadingCache<Class, Struct<List<Method>, List<Field>>> CACHE;
    /**
     * Cache of friendly data of a Class
     * This is data we assume we are allowed to see, usually has no fields as they're all encapsulated,
     * and any getters are included
     */
    private static final Cache<Class, Struct<List<Method>, List<Field>>> FRIENDLY_CACHE;

    static {
        CACHE = newBuilder()
                .weakKeys().weakValues()
                .build(new CacheLoader<Class, Struct<List<Method>, List<Field>>>() {
                    public Struct<List<Method>, List<Field>> load(Class key) {
                        return new Struct<>(methodsOfClass(key), fieldsOfClass(key));
                    }
                });

        FRIENDLY_CACHE = newBuilder()
                .weakKeys().weakValues()
                .build();
    }


    private ClassSearcher() {
    }

    @SneakyThrows
    public static Struct<List<Method>, List<Field>> dataOfClass(Class c, Options o) {
        if (o.isUnfriendly()) {
            return allDataOfClass(c);
        }
        return FRIENDLY_CACHE.get(c, () -> removeUnfriendly(allDataOfClass(c), o));
    }

    /**
     * Remove any Methods or Fields from a given Struct that is deemed unfriendly to be shown to the end user.
     * The implementation of this method can be found in {@link UnfriendlyFilter#filterMethods(List, Options)} and {@link UnfriendlyFilter#filterFields(List, Options)}
     *
     * @param struct A Struct containing a List<Method> to filter and a List<Field> to filter
     * @return A filtered Struct
     */
    private static Struct<List<Method>, List<Field>> removeUnfriendly(Struct<List<Method>, List<Field>> struct, Options o) {
        struct.setA(o.getFilter().filterMethods(struct.getA(), o));
        if (o.isIncludeFields())
            struct.setB(o.getFilter().filterFields(struct.getB(), o));
        else struct.setB(null);
        return struct;
    }


    private static Struct<List<Method>, List<Field>> allDataOfClass(Class c) {
        return CACHE.getUnchecked(c);
    }

    private static List<Field> fieldsOfClass(Class c) {
        return Lists.newArrayList(c.getDeclaredFields());
    }

    private static List<Method> methodsOfClass(Class c) {
        return Lists.newArrayList(c.getDeclaredMethods());
    }
}
