package uk.knightz.knightzapi.menu.adapter.token.types;

public interface AbstractGetter<C, T> {
    T getValue(C clazz);
}

