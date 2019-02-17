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

import lombok.Data;
import lombok.experimental.var;
import uk.knightz.knightzapi.utils.Struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Data
public class ClassSearcher {

    private static final Map<Class, Struct<List<Method>, List<Field>>> cache = new WeakHashMap<>();
    private static final Map<Class, Struct<List<Method>, List<Field>>> friendlyCache = new WeakHashMap<>();
    private boolean friendly;

    public Struct<List<Method>, List<Field>> dataOfClass(Class c, Options o) {
        this.friendly = o.isFriendly();
        var struct = allDataOfClass(c, o);
        if (friendly) {
            var friendlyStruct = friendlyCache.get(c);
            if (friendlyStruct != null) struct = friendlyStruct;

            else {
                struct = removeUnfriendly(struct, o);
                friendlyCache.put(c, struct);
            }
        }
        return struct;
    }

    /**
     * Remove any Methods or Fields from a given Struct that is deemed unfriendly to be shown to the end user.
     * The implementation of this method can be found in {@link ClassSearcher#filterMethods(List, Options)} and {@link ClassSearcher#filterFields(List, Options)}
     *
     * @param struct A Struct containing a List<Method> to filter and a List<Field> to filter
     * @return A filtered Struct
     */
    private Struct<List<Method>, List<Field>> removeUnfriendly(Struct<List<Method>, List<Field>> struct, Options o) {

        struct.setA(filterMethods(struct.getA(), o));
        if (o.isIncludeFields())
            struct.setB(filterFields(struct.getB(), o));
        return struct;
    }

    /**
     * Remove any Method objects from a given List of Method objects that is deemed unfriendly to be shown to the end user
     * <p>
     * A Method is deemed friendly if it doesn't supply all of the following conditions:
     * <p>
     * 1) The name of the Method contains "get" to mark it as a Getter (which typically signifies that the information is safe to be viewed)
     * 2) The method returns something other than void
     * 3) The method's access modifier is public - if it is not, the data it returns is assumed to be for internal use, hence the encapsulation
     * 4) The method must have a parameter count of 0
     * 5) The method must not be "getClass", as Class objects are deemed unfriendly, and can cause StackOverflowError due to {@link Class#getClassLoader()}
     *
     * @param a A list of Method object to filter
     * @param o The Options to use
     * @return A filtered list of Method objects
     */
    private List<Method> filterMethods(List<Method> a, Options o) {
        a.removeIf(m -> !m.getName().contains("get"));
        a.removeIf(m -> m.getReturnType().equals(Void.class));
        a.removeIf(m -> !Modifier.isPublic(m.getModifiers()));
        a.removeIf(m -> m.getParameterCount() > 0);
        a.removeIf(m -> m.getName().equals("getClass"));
        a.removeIf(m -> (o.getModifierBlacklist() & m.getModifiers()) != 0);
        return a;
    }

    /**
     * Remove any Field objects from a given List of Field objects that is deemed unfriendly to be shown to the end user.
     * <p>
     * Most fields of a class are usually encapsulated, so it's unlikely that any will be included.
     * Because of the unlikelihood of a public field, fields aren't scanned unless the given {@link Options} class has
     * {@link Options#isIncludeFields()} set to true
     * <p>
     * A Field is deemed unfriendly if it doesn't supply all of the following conditions.
     * 1) The Field must be public
     * 2) The Field must not be static, as this usually indicates a constant.
     * 3) The Field must not be transient
     *
     * @param a A List of Field objects to be filtered
     * @param o The Options to use
     * @return A filtered List of Field objects
     */
    private List<Field> filterFields(List<Field> a, Options o) {
        a.removeIf(f -> !Modifier.isPublic(f.getModifiers()));
        a.removeIf(f -> Modifier.isStatic(f.getModifiers()));
        a.removeIf(f -> Modifier.isTransient(f.getModifiers()));
        a.removeIf(f -> (o.getModifierBlacklist() & f.getModifiers()) != 0);
        return a;
    }


    private Struct<List<Method>, List<Field>> allDataOfClass(Class c, Options options) {
        var cached = cache.get(c);
        if (cached == null) {
            cached = new Struct<>(null, null);
            cached.setA(methodsOfClass(c));
            if (options.isIncludeFields())
                cached.setB(fieldsOfClass(c));
            cache.put(c, cached);
        }
        return cached;
    }

    private List<Field> fieldsOfClass(Class c) {
        return new ArrayList<>(Arrays.asList(c.getDeclaredFields()));
    }

    private List<Method> methodsOfClass(Class c) {
        return new ArrayList<>(Arrays.asList(c.getDeclaredMethods()));
    }
}
