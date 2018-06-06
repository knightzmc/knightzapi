package uk.knightz.knightzapi.menu.item;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.SubMenu;

public class BackMenuButton extends MenuButton {
	public BackMenuButton(ItemStack itemStack) {
		super(itemStack, e -> {
			if (e.isSubMenu()) {
				e.getWhoClicked().openInventory(((SubMenu) e.getClicked()).getParent().getInv());
			}
		});
	}
}
