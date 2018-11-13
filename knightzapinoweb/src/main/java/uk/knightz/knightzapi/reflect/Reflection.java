package uk.knightz.knightzapi.reflect;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import uk.knightz.knightzapi.utils.EnumUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Reflection {

    public static final Class[] simpleTypes = {Double.class, Float.class, Long.class, Integer.class, Short.class, Character.class, Byte.class, Boolean.class, Enum.class, String.class};
    private static final Map<Class, Set<Method>> cache = new WeakHashMap<>();

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
     *
     * @param clazz   The Class to retrieve public, user-friendly Getters of
     * @param options Includes a list of Methods to ignore, which will be removed from the Set. No other fields of {@link ReflectionOptions} are used
     * @return A sometimes empty Set of all of the Class's getters
     * @apiNote Caching is in place to speed up the otherwise slow Reflexive operations, so if any dynamic class modification is done at runtime, it is not guaranteed to update. Alternatively, the WeakReferences to the Class may be destroyed upon modification, causing new Getter generation
     */
    public static <T> Set<Method> getUserFriendlyPublicGetters(Class<T> clazz, ReflectionOptions<T> options) {
        if (cache.containsKey(clazz)) {
            return cache.get(clazz);
        }

        //Class classes cause StackOverflow errors because of getClassLoader()
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
     * Invoke all getters of an Object and put them into a Map with the Getter's friendly name as the key, and its returned value as the value
     * <p>
     * No caching is used as some getters may not always return the same value if the Object changes state.
     *
     * @param o The Object
     * @return The map
     */
    @SneakyThrows
    public static Map<String, Object> getAllNamesAndValuesOfObject(Object o, ReflectionOptions options) {
        Map<String, Object> ourMap = new HashMap<>();
        Set<Method> getters = getUserFriendlyPublicGetters(o.getClass(), options);
        for (Method g : getters) {
            if (!options.getMethodsToIgnore().contains(g)) {
                Object invoke = g.invoke(o);
                ourMap.put(StringUtils.capitalize(g.getName().replace("get", "")), invoke);
//                    if (!isSimpleType(invoke)) {
//                        ourMap.putAll(getAllNamesAndValuesOfObject(invoke, options));
//                    }
            }
        }
        return ourMap;
    }

}
