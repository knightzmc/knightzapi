package uk.knightz.knightzapi.menu.adapter.iface;

import uk.knightz.knightzapi.menu.adapter.options.Options;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Interface responsible for removing any data that shouldn't be shown to the end user
 * Default Implementation: {@link DefaultUnfriendlyFilter}
 */
public interface UnfriendlyFilter {
    /**
     * Remove any unfriendly Method objects from a given List of Method objects
     *
     * @param a A List to filter
     * @param o Any options to use
     * @return A filtered List
     */
    List<Method> filterMethods(List<Method> a, Options o);

    /**
     * Remove any unfriendly Field objects from a given List of Field objects
     * This usually isn't called, unless {@link Options#isIncludeFields()} is true
     *
     * @param a A List to filter
     * @param o Any options to use
     * @return A filtered List
     */
    List<Field> filterFields(List<Field> a, Options o);
}
