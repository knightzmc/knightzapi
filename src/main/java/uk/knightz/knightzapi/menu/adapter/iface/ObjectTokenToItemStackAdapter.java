package uk.knightz.knightzapi.menu.adapter.iface;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;

public interface ObjectTokenToItemStackAdapter<T> {

    ItemStack adapt(T data, ObjectToken<T> token, String obtainedFromName);

}
