package uk.knightz.knightzapi.menu;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.utils.MathUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A very resource heavy Function implementation that attempts to
 * convert a Collection of objects into a functionally immutable Menu (the user cannot interact with it)
 * <p>
 * As said, this operation uses lots of heavy reflection and String operations and although some caching is in place, should be used sparingly and ideally async to reduce resource load
 * <p>
 * A compatible data class will provide public getters for all information that should be added to the item, where one individual item represents one Object in the Collection
 * It also only returns Objects compatible with toString for a viable user experience
 * An example data class might look like this (uses Lombok annotations for brevity)
 * {@code
 *
 * @RequiredArgsConstructor public class User {
 * @Getter private String name //getName() will set the item name
 * , town; //All other data in the lore
 * <p>
 * private final long id; //no getter so will not be included
 * @Getter private Material itemType = UtilityClass.getMaterial(id); //some example method that corresponds a user id to a Material
 * //The getter will return a Material, so will be set as the item ID
 * }}
 */
public class CollectionToMenuAdapter {
    public static <T> Menu generateMenu(Collection<T> objects) {
        return generateMenu(objects, null);
    }

    public static <T> Menu generateMenu(Collection<T> objects, Option<T> options) {
        if (objects == null || objects.isEmpty()) return Menu.EMPTY_MENU;

        if (options == null) {
            options = new Option<>(null, null, null, null);
        }

        Menu menu = new Menu(objects.iterator().next().getClass().getSimpleName() + "s",
                MathUtils.roundUp(objects.size()) / 9);
        for (T t : objects) {
            if (t != null)
                menu.addButton(convert(t, options));
        }
        return menu;
    }

    @SneakyThrows
    private static <T> MenuButton convert(T o, Option<T> options) {
        if (o instanceof ItemStack) {
            return new MenuButton((ItemStack) o, Menu.cancel);
        }
        if (o instanceof MenuButton) {
            return (MenuButton) o;
        }
        Set<Method> getters = Reflection.getGetters(o.getClass());
        getters.removeIf(g -> options.methodsToIgnore.contains(g.getName()));
        Map<String, Object> returns = Reflection.getAllNamesAndValuesOfObject(o, options);

        //Create new ItemStack builder
        ItemBuilder i;
        if (options.hasManualItemStackFunction()) {
            i = new ItemBuilder(options.manualItemStackFunction.apply(o));
        } else {
            i = new ItemBuilder();
            Stream<Map.Entry<String, Object>> materialStream = returns.entrySet().stream().filter(o1 -> attemptConvert(o1.getValue()) != null);
            if (materialStream.count() > 0) {
                materialStream.forEach(m -> i.setType((Material) m.getValue()));
            } else {
                i.setType(Material.STONE);
            }
        }


        if (options.hasManualNameFunction()) {
            i.setName(options.manualNameFunction.apply(o));
        } else {
            Stream<Method> getterStream = getters.stream().filter(m -> m.getName().contains("name") && String.class.isAssignableFrom(m.getReturnType()));
            if (getterStream.count() > 0) {
                for (Iterator<Method> it = getterStream.iterator(); it.hasNext(); ) {
                    Method any = it.next();
                    i.setName((String) any.invoke(o));
                    getters.remove(any);
                }
            } else {
                i.setName(o.toString());
            }
        }

        List<String> lore = new ArrayList<>();
        for (Method m : getters) {
            Object value = m.invoke(o);
            if (value != null) {
                String parse = options.parse(value.getClass(), value);
                if (parse != null)
                    value = parse;
                String loreData = "&a" +
                        StringUtils.capitalize(m.getName().replace("get", ""))
                        + "&6: &7"
                        + value;
                lore.add(loreData);
            }
        }
        i.setLore(lore);

        return new MenuButton(i.build(), Menu.cancel);
    }

    private static Material attemptConvert(Object o) {
        if (o instanceof Material) return (Material) o;
        if (o instanceof String) {
            val material = Material.matchMaterial(((String) o).toUpperCase());
            if (material != null)
                return material;
        }
        return null;
    }

    public static class Reflection {

        public static final Class[] simpleTypes = {
                Double.class, Float.class, Long.class, Integer.class, Short.class, Character.class, Byte.class, Boolean.class, Enum.class, String.class
        };
        private static final Map<Class, Set<Method>> cache = new WeakHashMap<>();

        public static Set<Method> getGetters(Class<?> clazz) {
            if (cache.containsKey(clazz)) {
                return cache.get(clazz);
            }
            //Class classes cause StackOverFlow errors
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
            cache.put(clazz, methods);
            return methods;
        }

        public static boolean isSimpleType(Class clazz) {
            if (clazz.isEnum()) return true;
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
         * Invoke all getters of an Object and put them into a Map with the getter's friendly
         * name as the key, and the returned value as the value
         * <p>
         * No caching is used as some getters may not always return the same value.
         *
         * @param o The Object
         * @return The map
         */
        @SneakyThrows
        public static Map<String, Object> getAllNamesAndValuesOfObject(Object o, Option options) {
            Map<String, Object> ourMap = new HashMap<>();
            Set<Method> getters = getGetters(o.getClass());
            for (Method g : getters) {
                if (!options.methodsToIgnore.contains(g)) {
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


    public static class Option<T> {

        public static final Option EMPTY = new Option(new ArrayList<>(), new HashMap<>(), null, null);
        public List<String> methodsToIgnore;
        private Map<Class, Function<Object, String>> manualObjectParsers;
        private Function<T, ItemStack> manualItemStackFunction;
        private Function<T, String> manualNameFunction;

        public Option(List<String> methodsToIgnore, Map<Class, Function<Object, String>> manualObjectParsers, Function<T, ItemStack> manualItemStackFunction, Function<T, String> manualNameFunction) {
            this.methodsToIgnore = methodsToIgnore;
            this.manualObjectParsers = manualObjectParsers;
            this.manualItemStackFunction = manualItemStackFunction;
            this.manualNameFunction = manualNameFunction;
            addManualObjectParser(Player.class, HumanEntity::getName);
        }

        public static <T> OptionBuilder<T> builder() {
            return new OptionBuilder<>();
        }

        public <O> Option<T> addManualObjectParser(Class<O> m, Function<O, String> function) {
            manualObjectParsers.put(m, (Function<Object, String>) function);
            return this;
        }

        public String parse(Class clazz, Object type) {
            Class castedClass = objectParsersContainsClass(clazz);
            boolean parserExists = castedClass != null;
            if (parserExists) return manualObjectParsers.get(castedClass)
                    .apply(type);
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

            public Option<T> build() {
                return new Option<>(methodsToIgnore, manualObjectParsers, manualItemStackFunction, manualNameFunction);
            }

        }
    }
}
