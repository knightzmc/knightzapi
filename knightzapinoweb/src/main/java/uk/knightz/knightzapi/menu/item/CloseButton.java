package uk.knightz.knightzapi.menu.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;

public final class CloseButton extends MenuButton {
	private static final ItemStack DEFAULT = new ItemBuilder().setType(Material.REDSTONE_BLOCK).setName("&c&lClose")
			.setUnbreakable(true).build();

	public CloseButton() {
		this(DEFAULT);
	}

	public CloseButton(ItemStack itemStack) {
		super(itemStack, e -> e.getWhoClicked().closeInventory());
	}
}
