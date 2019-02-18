package uk.knightz.knightzapi.menu.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuButton;

import java.util.Collections;

public class OpenMenuButton extends MenuButton {


    private static final ItemStack DEFAULT_ITEM = new ItemBuilder()
            .setType(Material.BEACON)
            .setName("&a&lOpen Other Menu")
            .setLore(Collections.singletonList("&aClick to go to another Menu"))
            .build();

    public OpenMenuButton(Menu toOpen) {
        this(DEFAULT_ITEM, toOpen);
    }

    public OpenMenuButton(ItemStack item, Menu toOpen) {
        super(item, e -> toOpen.open(e.getWhoClicked()));
    }
}
