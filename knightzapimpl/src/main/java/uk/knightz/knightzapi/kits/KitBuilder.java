package uk.knightz.knightzapi.kits;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.utils.NotEmptyArrayList;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:25.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class KitBuilder {
    private String name = "Default";
    private ItemStack icon;
    private NotEmptyArrayList<KitItem> items;

    public KitBuilder(KitItem only) {
        this.items = new NotEmptyArrayList<>(only);
        this.icon = items.getFirst().getItem();
    }

    public KitBuilder(NotEmptyArrayList<KitItem> items) {
        this.items = items;
        this.icon = items.getFirst().getItem();
    }

    public KitBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public KitBuilder setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public KitBuilder setItems(NotEmptyArrayList<KitItem> items) {
        this.items = items;
        return this;
    }

    public KitBuilder addItem(KitItem item) {
        items.add(item);
        return this;
    }

    public Kit build() {
        return new Kit(name, icon, items);
    }
}
