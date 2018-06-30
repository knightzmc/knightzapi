package uk.knightz.knightzapi.menu.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.ClickEventAliases;
import uk.knightz.knightzapi.menu.MenuClickEvent;

import java.util.function.Consumer;

public final class CloseButton extends MenuButton {
	private static final ItemStack DEFAULT = new ItemBuilder().setType(Material.REDSTONE_BLOCK).setName("&c&lClose")
			.setUnbreakable(true).build();

	private static final Consumer<MenuClickEvent> onClick = e -> e.getWhoClicked().closeInventory();

	static {
		ClickEventAliases.getINSTANCE().add("close", onClick);
	}

	public CloseButton() {
		this(DEFAULT);
	}

	public CloseButton(ItemStack itemStack) {
		super(itemStack, onClick);
	}
}
