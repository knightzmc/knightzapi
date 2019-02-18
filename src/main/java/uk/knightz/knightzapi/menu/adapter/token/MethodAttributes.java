package uk.knightz.knightzapi.menu.adapter.token;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * A serializable, thread-safe store of most of the attributes of a certain Method object
 * It does not store a reference to the original Method, as this poses a security risk when serializing
 * It also doesn't store the return type of the Method because when stored in a Method token,
 * the return type can be obtained with {@link MethodToken#getValue()#getClass()}
 */
@Data
public class MethodAttributes<T> {

    private final String name;
    private final int modifiers;

    public static <V> MethodAttributes<V> ofMethod(Method method) {
        return new MethodAttributes<>(method.getName(), method.getModifiers());
    }
}
