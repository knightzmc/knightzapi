/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
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

package uk.knightz.knightzapi.menu.conversion;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.menu.button.MenuButtonBuilder;
import uk.knightz.knightzapi.reflect.Reflection;
import uk.knightz.knightzapi.reflect.ReflectionOptions;
import uk.knightz.knightzapi.utils.MathUtils;
import uk.knightz.knightzapi.utils.TripleStruct;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static uk.knightz.knightzapi.menu.conversion.CollectionToMenuAdapter.Type.BuildingUtils.tryToSetAmount;
import static uk.knightz.knightzapi.menu.conversion.CollectionToMenuAdapter.Type.BuildingUtils.tryToSetMaterial;
import static uk.knightz.knightzapi.reflect.Reflection.colorOfClass;


/**
 * A Class that attempts to convert a Collection of objects into a functionally immutable Menu (the user cannot interact with it, it is for display only)
 * <p>
 * This operation uses lots of heavy reflection and String operations and although caching is in place where it can be, should be used sparingly and ideally async to reduce resource load, especially with large Collections
 * <p>
 * A compatible data class will provide public getters for all information that should be added to the item, where one individual item represents one element in the given Collection
 * It also only returns Objects compatible with toString for a viable user experience
 * An example data class might look like this (uses Lombok annotations for brevity)
 * {@code
 * <p>
 * public class User {
 * <p>
 * private String name //getName() will set the item name
 * , town; //All other data in the lore
 * <p>
 * private final long id; //no getter so will not be included
 * <p>
 * private Material itemType = UtilityClass.getMaterial(id); //some example method that corresponds a user id to a Material
 * //getItemType() will return a Material, so will be set as the item type
 * }}
 */
public class CollectionToMenuAdapter {
    private static final String format = "%s - %s";

    public static <T> Menu generateMenu(Collection<T> objects) {
        return generateMenu(objects, null);
    }

    public static <T> Menu generateMenu(Collection<T> objects, ReflectionOptions<T> options) {
        if (objects == null || objects.isEmpty()) return Menu.EMPTY_MENU;

        if (options == null) {
            options = new ReflectionOptions<>(null, null, null, null);
        }

        Menu menu = new Menu(objects.iterator().next().getClass().getSimpleName() + "s", MathUtils.roundUp(objects.size()) / 9);
        for (T t : objects) {
            if (t != null)
                menu.addButton(convert(t, options));
        }
        return menu;
    }

    @SneakyThrows
    private static <T> MenuButton convert(T o, ReflectionOptions<T> options) {
        if (o instanceof ItemStack) {
            return new MenuButton((ItemStack) o, Menu.cancel);
        }
        if (o instanceof MenuButton) {
            return (MenuButton) o;
        }
        Set<Method> getters = Reflection.getUserFriendlyPublicGetters((Class<T>) o.getClass(), options);
        getters.removeIf(g -> options.getMethodsToIgnore().contains(g.getName()));
        Map<String, Object> returns = Reflection.getAllNamesAndValuesOfObject(o, options);

        //Create new ItemStack builder
        ItemBuilder i;
        if (options.hasManualItemStackFunction()) {
            i = new ItemBuilder(options.getManualItemStackFunction().apply(o));
        } else {
            i = new ItemBuilder();
//            Stream<Map.Entry<String, Object>> materialStream = returns.entrySet().stream().filter(o1 -> tryToSetMaterial(o1.getValue()) != null);
//            if (materialStream.count() > 0) {
//                materialStream.forEach(m -> i.setType((Material) m.getValue()));
//            } else {
//                i.setType(Material.STONE);
//            }
        }


        if (options.hasManualNameFunction()) {
            i.setName(options.getManualNameFunction().apply(o));
        } else {
            getters.forEach(g -> System.out.println(g.getName()));
            Optional<Method> getNameMethodOptional = getters.stream().filter(m -> m.getName().equals("getName") && String.class.isAssignableFrom(m.getReturnType())).findAny();
            if (getNameMethodOptional.isPresent()) {
                Method any = getNameMethodOptional.get();
                i.setName((String) any.invoke(o));
                getters.remove(any);
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
                System.out.println(value);
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

    /**
     * Maps a type of Object to a respective MenuButton generator.
     */
    enum Type {
        SIMPLE(true, s -> {
            MenuButtonBuilder builder = tryToSetMaterial(s.getC(), s.getB());

            builder = tryToSetAmount(builder, s.getB());

            builder.addLore(String.format(format, s.getA(), colorOfClass(s.getB().getClass()) + s.getB().toString()));
            return builder;
        }, Reflection.simpleTypes);
        //        EMPTY_COLLECTION_OR_MAP(true, s -> {
//
//        }, new Class[]{});
        private final List<Class> classesOfType;
        private final boolean includeSubClasses;
        /**
         * Struct Keys:
         * A: Friendly name of the method eg "getUUID()" becomes "UUID"
         * B: The Object that the method returned
         * C: The Builder being used
         */
        private final Function<TripleStruct<String, Object, MenuButtonBuilder>, MenuButtonBuilder> createButtonFunction;

        Type(boolean includeSubClasses, Function<TripleStruct<String, Object, MenuButtonBuilder>, MenuButtonBuilder> createButtonFunction, Class... classesOfType) {
            this.classesOfType = Arrays.asList(classesOfType);
            this.includeSubClasses = includeSubClasses;
            this.createButtonFunction = createButtonFunction;
        }

        public static MenuButton generate(Object o) {
            MenuButtonBuilder buttonBuilder = new MenuButtonBuilder();
            return buttonBuilder.buildButton();
        }

        private static Type typeOf(Object value) {
            return typeOf(value.getClass());
        }

        @SuppressWarnings("Duplicates")
        public static Type typeOf(Class type) {
            if (Reflection.isSimpleType(type)) {
                return SIMPLE;
            }
            for (Type value : values())
                if (value.includeSubClasses) for (Class clazz : value.classesOfType) {
                    if (clazz.isAssignableFrom(type)) return value;
                }
                else for (Class clazz : value.classesOfType)
                    if (clazz == type)
                        return value;
//            return DATA_OBJECT;
            return null;
        }

        static class BuildingUtils {
            static MenuButtonBuilder tryToSetMaterial(MenuButtonBuilder b, Object o) {
                if (b.getType() != null) { //don't overwrite materials
                    if (o instanceof Material) b.setType((Material) o);
                    if (o instanceof String) {
                        val material = Material.matchMaterial(((String) o).toUpperCase());
                        if (material != null)
                            b.setType(material);
                    }
                }
                return b;
            }

            static MenuButtonBuilder tryToSetAmount(MenuButtonBuilder b, Object o) {
                if (o instanceof Number) {
                    b.setAmount(((Number) o).intValue());
                }
                return b;
            }
        }
    }
}
