/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
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

package uk.knightz.knightzapi.menu.adapter;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuEvents;
import uk.knightz.knightzapi.menu.adapter.iface.ObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.options.Options;
import uk.knightz.knightzapi.menu.adapter.token.FieldToken;
import uk.knightz.knightzapi.menu.adapter.token.MethodToken;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.menu.adapter.token.factory.TokenFactory;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.menu.button.OpenMenuButton;
import uk.knightz.knightzapi.reflect.Reflection;
import uk.knightz.knightzapi.utils.ColorUtils;
import uk.knightz.knightzapi.utils.MathUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Powerful adapter class that takes a Collection (or array) of data and uses reflection to create an interactive Menu GUI
 * of this data that users can view (cannot currently change data).
 * <p>
 * It goes through the class of T and finds any getters (or fields and/or all methods depending on supplied options),
 * retrieves the given values for each instance of T in the Collection, and constructs a Menu from them
 * Extremely useful for reducing boilerplate and creating displays of information, however because it uses Reflection it is slow
 * Caching is implemented but we still use Reflection to call the getters to get their values
 *
 * @param <T> The Type of Object in the Collection
 */
public class CollectionToMenuAdapter<T> {

    /**
     * If the Object is complex (not a primitive, enum, or String)
     * we tell users that they can click to view details about it with this Lore String
     */
    private static final String VIEW_MORE_LORE = ChatColor.GREEN + "Click for more info";

    /**
     * String Format for the Title of a Menu showing details about an object
     * eg: "Person Info" when supplied with a Person object
     */
    private static final String VIEW_MORE_TITLE = ChatColor.GREEN + "%s Info";


    /**
     * Options used for adaption process and some Reflection
     */
    private Options options;
    /**
     * Adapter to change our ObjectToken objects into ItemStack objects
     * We can then add MenuButton functionality
     */
    private ObjectTokenToItemStackAdapter adapter;

    /**
     * Adapt a given Collection of objects into a Menu displaying data about these objects
     * <br>
     * This uses the default options, which only includes friendly, non-static getters in the Menu
     *
     * @param collection The Collection of objects to adapt
     * @return A Menu showing data about the given objects. If the Collection is empty, it returns null
     */
    public Menu adapt(Collection<T> collection) {
        return adapt(collection, Options.DEFAULT_OPTIONS);
    }

    /**
     * Adapt a given Collection of objects into a Menu displaying data about these objects
     *
     * @param collection The Collection of objects to adapt
     * @param options    Options to use. This defines if non-friendly information is included such as static or private methods
     * @return A Menu showing data about the given objects. If the Collection is empty, it returns null
     */
    public Menu adapt(Collection<T> collection, Options options) {
        if (collection.isEmpty()) {
            return null;
        }
        this.options = options;

        TokenFactory<T> factory = new TokenFactory<>();
        adapter = options.getObjectTokenToItemStackAdapter();

        T firstT = collection.iterator().next(); //allows us to access the class of T, the collection should never be empty
        Class<T> tClass = (Class<T>) firstT.getClass();
        String title = ColorUtils.colorOfClass(tClass) + tClass.getSimpleName() + "s";
        Menu menu = new Menu(title, collection.size());

        ObjectToken<T>[] generated = factory.generate(collection, options);
        Iterator<T> collectionIterator = collection.iterator();

        for (ObjectToken<T> token : generated) {
            if (token.hasNoValues()) {
                continue;
            }
            T t = collectionIterator.next();
            ItemStack adapted = adapter.adapt(t, token, null);
            if (Reflection.isSimpleType(t)) {
                menu.addButton(createSimpleButton(adapted, t));
            } else
                menu.addButton(createDisplayButton(menu, adapted, token, t));

        }

        return menu;
    }


    /**
     * Create a new MenuButton that displays when clicked, opens a new Menu with information about a corresponding Object
     */
    private MenuButton createDisplayButton(Menu parentMenu, ItemStack adapted, ObjectToken<T> token, T t) {
        String lore = VIEW_MORE_LORE;
        if (isEmpty(t)) {
            lore = ChatColor.RED + "List/Array/Map is empty.";
            if (adapted.hasItemMeta()) {
                ItemMeta itemMeta = adapted.getItemMeta();
                itemMeta.setLore(Collections.singletonList(lore));
                adapted.setItemMeta(itemMeta);
            }
            return createSimpleButton(adapted, t);
        }

        if (adapted.hasItemMeta()) {
            ItemMeta itemMeta = adapted.getItemMeta();
            itemMeta.setLore(Collections.singletonList(lore));
            adapted.setItemMeta(itemMeta);
        }
        return new MenuButton(adapted, e -> {
            e.setCancelled(true);
            Menu displayMenu = createDisplayMenu(parentMenu, token, t);
            displayMenu.open(e.getWhoClicked());
        });
    }


    private Menu createDisplayMenu(Menu parentMenu, ObjectToken<T> token, T t) {
        if (t instanceof Collection) {
            return adapt((Collection<T>) t, options);
        }
        int size = token.getFieldTokens().size() + token.getMethodTokens().size();
        if (size == 0) {
            size = 1;
        }
        Menu menu = new Menu(String.format(VIEW_MORE_TITLE, t.getClass().getSimpleName()), MathUtils.roundUp(size) / 9);

        menu.addButton(new OpenMenuButton(parentMenu));
        for (MethodToken methodToken : token.getMethodTokens()) {
            menu.addButton(menuButtonOfToken(menu, methodToken));
        }
        for (FieldToken fieldToken : token.getFieldTokens()) {
            menu.addButton(menuButtonOfToken(menu, fieldToken));
        }
        return menu;
    }

    private MenuButton menuButtonOfToken(Menu parentMenu, DataToken<Object, T> dataToken) {
        T value = dataToken.getValue();
        ObjectToken token = new TokenFactory<>().generate(value, options);

        ItemStack adapted = adapter.adapt(value, token, dataToken.getFriendlyDataName());
        if (Reflection.isSimpleType(dataToken.getType())) {
            return createSimpleButton(adapted, value);
        } else {
            return createDisplayButton(parentMenu, adapted, token, value);
        }
    }

    private MenuButton createSimpleButton(ItemStack adapted, T t) {
        return new MenuButton(adapted, MenuEvents.CANCEL);
    }

    private boolean isEmpty(T t) {
        if (t instanceof Collection) return ((Collection) t).isEmpty();
        if (t.getClass().isArray()) {
            return Array.getLength(t) == 0;
        }
        if (t instanceof Map) {
            return ((Map) t).isEmpty();
        }
        return false;
    }
}
