package uk.knightz.knightzapi.menu.adapter;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuButton;
import uk.knightz.knightzapi.menu.MenuEvents;
import uk.knightz.knightzapi.menu.adapter.iface.ObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.options.Options;
import uk.knightz.knightzapi.menu.adapter.token.FieldToken;
import uk.knightz.knightzapi.menu.adapter.token.MethodToken;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.menu.adapter.token.factory.TokenFactory;
import uk.knightz.knightzapi.menu.button.OpenMenuButton;
import uk.knightz.knightzapi.reflect.Reflection;
import uk.knightz.knightzapi.utils.ColorUtils;
import uk.knightz.knightzapi.utils.MathUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class CollectionToMenuAdapter<T> {

    private static final String TITLE_FORMAT = "%s%s";

    private static final String VIEW_MORE_LORE = ChatColor.GREEN + "Click for more info";

    private static final String VIEW_MORE_TITLE = ChatColor.GREEN + "%s Info";


    private Options options;
    private ObjectTokenToItemStackAdapter adapter;

    public Menu adapt(Collection<T> ts) {
        return adapt(ts, Options.defaultOptions);
    }

    public Menu adapt(Collection<T> collection, Options options) {
        if (collection.isEmpty()) {
            return null;
        }
        this.options = options;
        TokenFactory<T> factory = new TokenFactory<>();
        adapter = options.getObjectTokenToItemStackAdapter();

        T firstT = collection.iterator().next(); //allows us to access the class of T, the collection should never be empty
        Class<T> tClass = (Class<T>) firstT.getClass();
        String title = String.format(TITLE_FORMAT, ColorUtils.colorOfClass(tClass), tClass.getSimpleName() + "s");
        Menu menu = new Menu(title, collection.size());

        ObjectToken<T>[] generated = factory.generate(collection, options);
        Iterator<T> collectionIterator = collection.iterator();

        for (ObjectToken<T> token : generated) {
            T t = collectionIterator.next();
            ItemStack adapted = adapter.adapt(t, token, null);
            if (Reflection.isSimpleType(t)) {
                menu.addButton(createSimpleButton(adapted, t));
            } else
                menu.addButton(createDisplayButton(menu, adapted, token, t));

        }

        return menu;
    }

    private MenuButton createDisplayButton(Menu parentMenu, ItemStack adapted, ObjectToken<T> token, T t) {
        if (adapted.hasItemMeta()) {
            ItemMeta itemMeta = adapted.getItemMeta();
            itemMeta.setLore(Collections.singletonList(VIEW_MORE_LORE));
            adapted.setItemMeta(itemMeta);
        }

        Menu displayMenu = createDisplayMenu(parentMenu, token, t);
        if (displayMenu == null) return null;
        return new MenuButton(adapted, e -> {
            e.setCancelled(true);
            displayMenu.open(e.getWhoClicked());
        });
    }

    private Menu createDisplayMenu(Menu parentMenu, ObjectToken<T> token, T t) {
        int size = token.getFieldTokens().size() + token.getMethodTokens().size();
        if (size == 0) return null;
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
        return new MenuButton(adapted, MenuEvents.CANCEL_CLICK);
    }
}
