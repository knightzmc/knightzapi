package uk.knightz.knightzapi.menu.adapter.iface;

import uk.knightz.knightzapi.menu.adapter.options.Options;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class DefaultUnfriendlyFilter implements UnfriendlyFilter {
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
     * 6) The method must not be static.
     *
     * @param a A list of Method object to filter
     * @param o The Options to use
     * @return A filtered list of Method objects
     */
    @Override
    public List<Method> filterMethods(List<Method> a, Options o) {
        a.removeIf(m ->
                !m.getName().contains("get") ||
                        m.getReturnType().equals(Void.class) ||
                        !Modifier.isPublic(m.getModifiers()) ||
                        m.getParameterCount() > 0 ||
                        m.getName().equals("getClass") ||
                        (o.getModifierBlacklist() & m.getModifiers()) != 0);
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
    @Override
    public List<Field> filterFields(List<Field> a, Options o) {
        a.removeIf(f ->
                !Modifier.isPublic(f.getModifiers())
                        || Modifier.isStatic(f.getModifiers())
                        || Modifier.isTransient(f.getModifiers())
                        || (o.getModifierBlacklist() & f.getModifiers()) != 0);
        return a;
    }
}
