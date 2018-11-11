package uk.knightz.knightzapi.reflect;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.conversion.CollectionToMenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Wrapper class defining a number of Reflexive operation options that are used in any automatic generation classes such as {@link CollectionToMenuAdapter} and {@link uk.knightz.knightzapi.lang.fancy.SuperFancyMessageFactory}
 * The {@link ReflectionOptions#methodsToIgnore} is also used when collecting a Set of Getters in {@link Reflection#getUserFriendlyPublicGetters(Class, ReflectionOptions)}
 *
 * @param <T>
 */
@Getter
public class ReflectionOptions<T> {

    public static final ReflectionOptions EMPTY = new ReflectionOptions(new ArrayList<>(), new HashMap<>(), null, null);
    private List<String> methodsToIgnore;
    private Map<Class, Function<Object, String>> manualObjectParsers;
    private Function<T, ItemStack> manualItemStackFunction;
    private Function<T, String> manualNameFunction;

    public ReflectionOptions(List<String> methodsToIgnore, Map<Class, Function<Object, String>> manualObjectParsers, Function<T, ItemStack> manualItemStackFunction, Function<T, String> manualNameFunction) {
        this.methodsToIgnore = methodsToIgnore;
        this.manualObjectParsers = manualObjectParsers;
        this.manualItemStackFunction = manualItemStackFunction;
        this.manualNameFunction = manualNameFunction;
        addManualObjectParser(Player.class, HumanEntity::getName);
    }

    public static <T> OptionBuilder<T> builder() {
        return new OptionBuilder<>();
    }

    public <O> ReflectionOptions<T> addManualObjectParser(Class<O> m, Function<O, String> function) {
        manualObjectParsers.put(m, (Function<Object, String>) function);
        return this;
    }

    public String parse(Class clazz, Object type) {
        Class castedClass = objectParsersContainsClass(clazz);
        boolean parserExists = castedClass != null;
        if (parserExists) return manualObjectParsers.get(castedClass).apply(type);
        return type.toString();
    }

    private Class objectParsersContainsClass(Class clazz) {
        if (manualObjectParsers.containsKey(clazz)) return clazz;
        for (Class c : manualObjectParsers.keySet()) {
            if (c.isAssignableFrom(clazz) && c != clazz)
                return c;
        }
        return null;
    }

    public boolean hasManualNameFunction() {
        return manualNameFunction != null;
    }

    public boolean hasManualItemStackFunction() {
        return manualItemStackFunction != null;
    }

    public boolean hasAnyObjectParsers() {
        return manualObjectParsers != null && !manualObjectParsers.isEmpty();
    }

    public static class OptionBuilder<T> {
        @NonNull
        private List<String> methodsToIgnore = new ArrayList<>();
        private Function<T, ItemStack> manualItemStackFunction;
        private Function<T, String> manualNameFunction;
        private Map<Class, Function<Object, String>> manualObjectParsers = new HashMap<>();

        public OptionBuilder() {
            addManualObjectParser(Player.class, HumanEntity::getName);
        }

        public OptionBuilder<T> setManualObjectParsers(Map<Class, Function<Object, String>> manualObjectParsers) {
            this.manualObjectParsers.putAll(manualObjectParsers);
            return this;
        }

        public OptionBuilder<T> addMethodToIgnore(String method) {
            methodsToIgnore.add(method);
            return this;
        }

        public <O> OptionBuilder<T> addManualObjectParser(Class<O> m, Function<O, String> function) {
            manualObjectParsers.put(m, (Function<Object, String>) function);
            return this;
        }

        public OptionBuilder<T> manualItemStackFunction(Function<T, ItemStack> manualItemStackFunction) {
            this.manualItemStackFunction = manualItemStackFunction;
            return this;
        }

        public OptionBuilder<T> manualNameFunction(Function<T, String> manualNameFunction) {
            this.manualNameFunction = manualNameFunction;
            return this;
        }

        public ReflectionOptions<T> build() {
            return new ReflectionOptions<>(methodsToIgnore, manualObjectParsers, manualItemStackFunction, manualNameFunction);
        }
    }
}
