package uk.knightz.knightzapi.menuold.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menuold.Menu;
import uk.knightz.knightzapi.menuold.MenuClickEvent;
import uk.knightz.knightzapi.menuold.Page;

public final class BackPageButton extends MenuButton {


    private static final ItemStack defaultItem = new ItemBuilder()
            .setType(Material.REDSTONE)
            .setName("&c&lBack")
            .build();

    /**
     * Create a new Back Page Button
     *
     * @param itemStack The ItemStack that will be added to a menuold.
     */
    public BackPageButton(ItemStack itemStack) {
        super(itemStack,
                BackPageButton::accept);
    }

    /**
     * Create a new Back Page Button with the default back button
     */
    public BackPageButton() {
        super(defaultItem, BackPageButton::accept);
    }

    private static void accept(MenuClickEvent e) {
        System.out.println("e.getMenu() instanceof Page = " + (e.getMenu() instanceof Page));
        if (e.getMenu() instanceof Page) {
            Menu toOpen;
            Page current = (Page) e.getMenu();
            System.out.println(KnightzAPI.gson.toJson(current));
            int indexOf = e.getMenu().getPages().indexOf(current);
            if (indexOf == -1) {
                toOpen = current.getParent();
            } else {
                toOpen = e.getMenu().getPages().get(indexOf);
            }
            toOpen.open(e.getWhoClicked());
        }
    }
}
