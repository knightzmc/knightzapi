package uk.knightz.knightzapi.kits;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.utils.NotEmptyArrayList;

/**
 * This class was created by AlexL (Knightz) on 03/02/2018 at 20:51.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class PurchasableKit extends Kit {

    private final double price;

    protected PurchasableKit(String name, ItemStack icon, NotEmptyArrayList<KitItem> items, double price) throws KitAlreadyExistsException {
        super(name, icon, items);
        this.price = price;
    }

    protected PurchasableKit(String name, NotEmptyArrayList<KitItem> items, double price) throws KitAlreadyExistsException {
        super(name, items);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
