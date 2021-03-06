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

package uk.knightz.knightzapi.reflect;

import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Reflection {

    public static final Class[] simpleTypes = {Double.class, Float.class, Long.class, Integer.class, Short.class, Character.class, Byte.class, Boolean.class, Enum.class, String.class};
    private static final Map<Class, Set<Method>> cache = new WeakHashMap<>();

    /**
     * Check if a given class is of a "simple" type, meaning:
     * 1) It is a wrapper of a Primitive Type such as {@link Integer}
     * 2) It represents a Primitive Type, such as <code>int.class</code>
     * 3) It is an Enum or a String - Strings are classified as simple as they can be easily made user-friendly with  {@link String#toString}
     *
     * @param clazz The Class to check
     * @return If the given Class is of a simple type
     */
    public static boolean isSimpleType(Class clazz) {
        if (clazz.isEnum()) return true;
        if (clazz.isPrimitive()) return true;
        for (Class simple : simpleTypes) {
            if (simple == clazz || clazz.isAssignableFrom(simple)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSimpleType(Object o) {
        if (o == null)
            return false;
        return isSimpleType(o.getClass());
    }


    /**
     * Get all of the public Getter Methods of a Class that are deemed to be friendly to expose to the User
     * A Method is classified as a public Getter and User-Friendly if:
     * <br>
     * 1) The name of the Method contains "get" - Accessor functions are not supported
     * <br>
     * 2) The method returns something other than an Object of void type
     * <br>
     * 3) The method's access modifier is public - if it is not, the data it returns is assumed to be for internal use, hence the encapsulation
     * <br>
     * 4) The method must have a parameter count of 0
     * <br>
     * 5) The method inherited from Object - "getClass" is not included as Class objects are not considered useful or friendly to show to the User
     * <br>
     * 6) Because of this, if the supplied "clazz" is of type Class, an empty Set will be returned, as the method {@link Class#getClassLoader()} causes StackOverflow errors.
     * <p>
     * Caching is in place to speed up the otherwise slow reflexive operations, so if any dynamic class modification is done at runtime, it is not guaranteed to update. Alternatively, the WeakReferences to the Class may be destroyed upon modification, causing new Getter generation
     *
     * @param clazz   The Class to retrieve public, user-friendly Getters of
     * @param options Includes a list of Methods to ignore, which will be removed from the Set. No other fields of {@link ReflectionOptions} are used
     * @param <T>     The type of Class
     * @return A sometimes empty Set of all of the Class's getters
     */
    public static <T> Set<Method> getUserFriendlyPublicGetters(Class<T> clazz, ReflectionOptions<T> options) {
        if (cache.containsKey(clazz)) {
            return cache.get(clazz);
        }
        String s;
        //Class classes cause StackOverflow errors because of getClassLoader()
        //and any stored class probably shouldn't be exposed anyway
        if (clazz.equals(Class.class)) {
            return Collections.emptySet();
        }
        Set<Method> methods = new HashSet<>(Arrays.asList(clazz.getMethods()));
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        methods.removeIf(m -> !m.getName().contains("get"));
        methods.removeIf(m -> m.getName().equals("getClass"));
        methods.removeIf(m -> m.getReturnType().equals(void.class));
        methods.removeIf(m -> !Modifier.isPublic(m.getModifiers()));
        methods.removeIf(m -> m.getParameters().length >= 1);
        methods.removeIf(m -> options.getMethodsToIgnore().contains(m.getName()));
        cache.put(clazz, methods);
        return methods;
    }

    /**
     * Invoke all getters of an Object and put them into a Map with the Getter's friendly name as the key, and its returned value as the value
     * <p>
     * No caching is used as some getters may not always return the same value if the Object changes state.
     *
     * @param o       The Object
     * @param options Options to use
     * @return The map
     */
    @SneakyThrows
    public static Map<String, Object> getAllNamesAndValuesOfObject(Object o, ReflectionOptions options) {
        Map<String, Object> namesAndValuesMap = new HashMap<>();
        Set<Method> getters = getUserFriendlyPublicGetters(o.getClass(), options);
        for (Method g : getters) {
            if (!options.getMethodsToIgnore().contains(g)) {
                Object invoke = g.invoke(o);
                if (invoke != null)
                    namesAndValuesMap.put(StringUtils.capitalize(g.getName().replace("get", "")), invoke);
//                    if (!isSimpleType(invoke)) {
//                        namesAndValuesMap.putAll(getAllNamesAndValuesOfObject(invoke, options));
//                    }
            }
        }
        return namesAndValuesMap;
    }

    public static <V> V callMethod(Method method, Object object, Object... args) {
        if (method == null) return null;
        if (object == null) return null;
        try {
            return (V) method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object valueOfField(Field field, Object retrieveValuesFrom) {
        if (field == null) return null;
        if (retrieveValuesFrom == null) return null;
        try {
            return field.get(retrieveValuesFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void setFieldValue(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempt to get a Setter method from a Getter method
     * If the provided method is something like int getAge(),
     * the respective Setter must be setAge(int age)
     *
     * @param method
     * @return
     */
    public static Method getSetterOfGetter(Method method) {
        if (!isGetter(method)) return null;
        String getterName = method.getName();
        String variableName = method.getName().substring(3); //remove "get"
        String setterName = "set".concat(variableName);

        return getPublicMethod(method.getDeclaringClass(), setterName, method.getReturnType());
    }

    public static Method getPublicMethod(Class clazz, String name, Class... parameters) {
        Method method;
        try {
            method = clazz.getMethod(name, parameters);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getDeclaredMethod(name, parameters);
            } catch (NoSuchMethodException e1) {
                return null;
            }
        }
        if (!Modifier.isPublic(method.getModifiers())) return null;
        return method;
    }


    public static boolean isGetter(Method method) {
        return method.getName().contains("get")
                && method.getParameterCount() == 0
                && method.getReturnType() != Void.class;
    }
}
