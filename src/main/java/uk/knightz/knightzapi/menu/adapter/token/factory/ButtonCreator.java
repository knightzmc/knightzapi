package uk.knightz.knightzapi.menu.adapter.token.factory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.adapter.options.Options;
import uk.knightz.knightzapi.menu.adapter.token.FieldToken;
import uk.knightz.knightzapi.menu.adapter.token.MethodToken;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.menu.button.OpenMenuButton;
import uk.knightz.knightzapi.menu.button.builder.MenuButtonBuilder;
import uk.knightz.knightzapi.reflect.Reflection;
import uk.knightz.knightzapi.utils.MathUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ButtonCreator<T> {
    private static final String EMPTY_LORE = ChatColor.RED + "List/Array/Map is empty.";
    private static final String SET_VALUE_LORE = ChatColor.GREEN + "Right Click to set value";
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

    private final Options options;

    public ButtonCreator(Options options) {
        this.options = options;
    }

    public MenuButton convert(ItemStack adapted, T t, ObjectToken<T> token, DataToken<Object, T> source, Menu parentMenu) {
        if (Reflection.isSimpleType(t)) {
            return createSimpleDisplayButton(adapted, t, token, source);
        }
        return createDisplayButton(adapted, t, token, source, parentMenu); //create a button of a complex object
    }

    private MenuButton createDisplayButton(ItemStack adapted, T t, ObjectToken<T> token, DataToken<Object, T> source, Menu parentMenu) {
        return new MenuButtonBuilder(adapted)
                .onAnyClick().thenCancel().thenOpenMenu(
                        createDisplayMenu(adapted, t, token, source, parentMenu)).done().buildButton();
    }

    private Menu createDisplayMenu(ItemStack adapted, T t, ObjectToken<T> token, DataToken<Object, T> source, Menu parentMenu) {
        int size = token.getFieldTokens().size() + token.getMethodTokens().size();
        if (size == 0) {
            size = 1;
        }
        Menu menu = new Menu(String.format(VIEW_MORE_TITLE, t.getClass().getSimpleName()), MathUtils.roundUp(size) / 9);
        ObjectToken subToken = new TokenFactory<>().generate(t);

        menu.addButton(new OpenMenuButton(parentMenu));
        for (MethodToken methodToken : token.getMethodTokens()) {
            menu.addButton(menuButtonOfToken(methodToken, menu));
        }
        for (FieldToken fieldToken : token.getFieldTokens()) {
            menu.addButton(menuButtonOfToken(fieldToken, menu));
        }
        return menu;
    }

    private MenuButton menuButtonOfToken(DataToken<Object, T> dataToken, Menu parentMenu) {
        T value = dataToken.getValue();
        ObjectToken token = new TokenFactory<>().generate(value, options);

        ItemStack adapted = options.getObjectTokenToItemStackAdapter().adapt(value, token, dataToken.getFriendlyDataName());
        if (Reflection.isSimpleType(dataToken.getType())) {
            return createSimpleDisplayButton(adapted, value, token, dataToken);
        } else {
            return createDisplayButton(adapted, value, token, dataToken, parentMenu);
        }
    }


    private MenuButton createSimpleDisplayButton(ItemStack adapted, T t, ObjectToken<T> token, DataToken<Object, T> source) {
        if (options.allowSettingValues() && source != null && source.hasSettingFunctionality()) {
            addLore(adapted, SET_VALUE_LORE);
        }
        return new MenuButtonBuilder(adapted)
                .onAnyClick().thenCancel().done()
                .onClick(ClickType.RIGHT)
                .ifTrue(options::allowSettingValues).ifTrue(() -> source != null).ifTrue(source::hasSettingFunctionality)
                .thenCancel().thenAction(e -> {
                    Future<?> future = UserDataSupplierFactory.getDataSupplier(t.getClass())
                            .apply(e.getWhoClicked());
                    Bukkit.getScheduler().runTaskAsynchronously(KnightzAPI.getP(), () -> {
                        Object o = source.getValue();
                        try {
                            o = future.get();
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                        source.getSetter().setValue(t, o);
                        e.getMenu().open(e.getWhoClicked());
                    });
                }).done().buildButton();
    }


    private boolean isCollectionOrArray(T t) {
        return t instanceof Collection || t.getClass().isArray();
    }


    private boolean isMutableCollectionOrArray(T t) {
        if (t instanceof Collection) {
            try {
                ((Collection) t).addAll(Collections.emptyList());
                return true;
            } catch (Exception e) {
                return false;
            }
        } else return t.getClass().isArray();
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

    private ItemStack addLore(ItemStack i, String... lore) {
        if (i.hasItemMeta()) {
            ItemMeta itemMeta = i.getItemMeta();
            List<String> itemLore = itemMeta.getLore();
            itemLore.addAll(Arrays.asList(lore));
            itemMeta.setLore(itemLore);
            i.setItemMeta(itemMeta);
        }
        return i;
    }

}
