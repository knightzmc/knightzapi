package uk.knightz.knightzapi.menu.item;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.ClickEventAliases;
import uk.knightz.knightzapi.menu.MenuClickEvent;
import uk.knightz.knightzapi.menu.SubMenu;

import java.util.function.Consumer;

public class BackMenuButton extends MenuButton {
	private static final Consumer<MenuClickEvent> onClick = e -> {
		if (e.isSubMenu()) {
			e.getWhoClicked().openInventory(((SubMenu) e.getClicked()).getParent().getInv());
		}
	};

	static {
		ClickEventAliases.getINSTANCE().add("back", onClick);
	}

	public BackMenuButton(ItemStack itemStack) {
		super(itemStack, onClick);
	}
}
