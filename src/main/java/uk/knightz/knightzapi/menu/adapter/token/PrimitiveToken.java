package uk.knightz.knightzapi.menu.adapter.token;

import java.util.ArrayList;
import java.util.Collections;

public class PrimitiveToken<T> extends ObjectToken<T> {

    public PrimitiveToken(T value) {
        super(new ArrayList<>(), Collections.singletonList(
                new MethodToken<>(
                        new MethodAttributes<>("getValue", 1), //create a fake MethodAttributes class to replicate the value of a primitive type
                        value)
        ));
    }
}
