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

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.adapter.iface.ObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.options.Options;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.factory.ButtonCreator;
import uk.knightz.knightzapi.menu.adapter.token.factory.TokenFactory;
import uk.knightz.knightzapi.utils.ColorUtils;

import java.util.Collection;
import java.util.Iterator;

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

            ButtonCreator creator = new ButtonCreator(options);
            menu.addButton(creator.convert(adapted, t, token, null, menu));
//
//            if (Reflection.isSimpleType(t)) menu.addButton(createSimpleButton(adapted, t));
//
//            else if (isEmpty(t)) menu.addButton(createEmptyCollectionButton(adapted, t));
//
//            else menu.addButton(createDisplayButton(menu, adapted, token, t));

        }

        return menu;
    }


}
