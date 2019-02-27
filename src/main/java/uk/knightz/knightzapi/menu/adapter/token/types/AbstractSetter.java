package uk.knightz.knightzapi.menu.adapter.token.types;

public interface AbstractSetter<C, V> {

    /**
     * Call the setter, setting a given value
     *
     * @param type  The object to call the setter on. Not always applicable
     * @param value The value to set
     */
    void setValue(C type, V value);
}
